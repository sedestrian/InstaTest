package com.alessandrogaboardi.instatest.activities

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.webkit.URLUtil
import android.widget.ImageView
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.kotlin.extensions.realm
import com.alessandrogaboardi.instatest.kotlin.extensions.setGone
import com.alessandrogaboardi.instatest.kotlin.extensions.setVisible
import com.alessandrogaboardi.instatest.kotlin.extensions.snack
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.LikeMediaCallback
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_media_detail.*
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.downloadManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ActivityMediaDetail : AppCompatActivity() {
    var id: String = ""
    var working = false
    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 29102
    var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_detail)

        supportPostponeEnterTransition()

        intent?.let {
            id = it.getStringExtra(MEDIA_ID_EXTRA)
        }

        loadData()
    }

    private fun loadData() {
        val result = DaoMedia.getByIdAsync(id)
        result.addChangeListener<ModelMedia> { mediaObject ->
            val media = if (mediaObject.isManaged)
                realm.copyFromRealm(mediaObject)
            else
                mediaObject

            result.removeAllChangeListeners()

            setupViews(media)
        }
    }

    private fun setupViews(media: ModelMedia) {
        val hasLiked = media.user_has_liked
        val likesNumber = media.likes?.count
        val commentsNumber = media.comments?.count
        val user_pic = media.user?.profile_picture
        val user = media.user?.username
        val caption = media.caption?.text

        likes.text = likesNumber.toString()
        comments.text = commentsNumber.toString()
        username.text = user
        description.text = caption

        if (description.text.isEmpty()) description.setGone()
        else description.setVisible()

        updateLikeIcon(media)

        liked.setOnClickListener {
            if (!working) {
                working = true
                if (media.user_has_liked) {
                    dislikeMedia(media)
                } else {
                    likeMedia(media)
                }
            }
        }

        save.setOnClickListener {
            saveFileWithPermissions(media)
        }

        if (media.isVideo()) {
            setShareHandler(media)
            picture.setGone()
            videoView.setPreviewImage(media.images?.standard_resolution?.url)
            videoView.setVideoURL(media.videos?.standard_resolution?.url)
            videoView.setOnPreviewLoaded {
                supportStartPostponedEnterTransition()
            }
            videoView.setOnPreviewLoadError { supportStartPostponedEnterTransition() }
        } else {
            GlideApp.with(this).load(media.images?.standard_resolution?.url)
                    .dontAnimate()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            pictureErrorLayout.setVisible()
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            pictureErrorLayout.setGone()
                            setShareHandler(media)
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(picture)
            picture.setOnClickListener {
                val intent = Intent(this, ActivityFullscreenPicture::class.java)
                intent.putExtra(ActivityFullscreenPicture.PICTURE_ID_EXTRA, media.id)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        picture,
                        getString(R.string.fullscreen_element_name)
                )

                startActivity(intent, options.toBundle())
            }
        }
        GlideApp.with(this).load(user_pic).circleCrop().into(userPicture)
    }

    fun setShareHandler(media: ModelMedia) {
        share?.setOnClickListener {
            if (media.isVideo()) {
                this@ActivityMediaDetail.videoView.shareVideo()
            } else {
                onShareItem()
            }
        }
    }

    fun onShareItem() {
        // Get access to the URI for the bitmap
        val bmpUri = getLocalBitmapUri(picture)
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
            shareIntent.type = "image/*"
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        } else {
            // ...sharing failed, handle error
        }
    }

    private fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable;
        var bmp: Bitmap? = null;
        if (drawable is BitmapDrawable) {
            bmp = (imageView.drawable as BitmapDrawable).bitmap;
        } else {
            return null
        }
        // Store image to default external storage directory
        var bmpUri: Uri? = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
            val out = FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (e: IOException) {
            e.printStackTrace();
        }
        return bmpUri
    }

    private fun updateLikeIcon(item: ModelMedia) {
        if (item.user_has_liked) {
            liked.setImageResource(R.drawable.full_heart)
        } else {
            liked.setImageResource(R.drawable.empty_heart)
        }

        println(item.likes?.count)
        this.likes.text = item.likes?.count.toString()
    }

    private fun dislikeMedia(media: ModelMedia) {
        ApiManager.dislikeMedia(media.id, object : LikeMediaCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                DaoMedia.setDisliked(media.id)
                media.user_has_liked = false
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count - 1
                }
                updateLikeIcon(media)
                working = false
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(picture, getString(R.string.error_dislike), true)
                working = false
            }
        })
    }

    private fun likeMedia(media: ModelMedia) {
        ApiManager.likeMedia(media.id, object : LikeMediaCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                DaoMedia.setLiked(media.id)
                media.user_has_liked = true
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count + 1
                }
                updateLikeIcon(media)
                working = false
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(picture, getString(R.string.error_like), true)
                working = false
            }
        })
    }

    fun saveFileWithPermissions(item: ModelMedia) {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            downloadPicture(item)
        } else {
            askPermissions()
        }
    }

    private fun askPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun downloadPicture(item: ModelMedia) {
        val direct = File(Environment.DIRECTORY_DOWNLOADS + getString(R.string.pictures_folder))
        val filename = URLUtil.guessFileName(item.images?.standard_resolution?.url, null, null)

        if (!direct.exists()) {
            direct.mkdirs()
        }

        val mgr = downloadManager

        val downloadUri = Uri.parse(item.images?.standard_resolution?.url)
        val request = DownloadManager.Request(
                downloadUri)

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setDescription("Coonstagram")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)


        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        setDownloading()
        mgr.enqueue(request)
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            unregisterReceiver(this)
            setCompleted()
        }
    }

    private fun setDownloading() {
        save.setGone()
        progress.setVisible()
    }

    private fun setCompleted() {
        progress.setGone()
        save.setVisible()
    }

    companion object {
        val MEDIA_ID_EXTRA = "media_detail_id_extra"
    }
}
