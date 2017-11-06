package com.alessandrogaboardi.instatest.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import org.jetbrains.anko.runOnUiThread
import java.io.IOException

class ActivityMediaDetail : AppCompatActivity() {
    var id: String = ""

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
            val media = if(mediaObject.isManaged)
                realm.copyFromRealm(mediaObject)
            else
                mediaObject

            result.removeAllChangeListeners()

            setupViews(media)
        }
    }

    private fun setupViews(media: ModelMedia){
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
            if (hasLiked) {
                dislikeMedia(media)
            } else {
                likeMedia(media)
            }
        }

        GlideApp.with(this).load(media.images?.standard_resolution?.url)
                .dontAnimate()
                .listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(picture)
        GlideApp.with(this).load(user_pic).circleCrop().into(userPicture)
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
                runOnUiThread {
                    DaoMedia.setDisliked(media.id)
                    media.user_has_liked = false
                    if(media.likes != null){
                        media.likes!!.count = media.likes!!.count - 1
                    }
                    updateLikeIcon(media)
                }
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(picture, getString(R.string.error_dislike), true)
            }
        })
    }

    private fun likeMedia(media: ModelMedia) {
        ApiManager.likeMedia(media.id, object : LikeMediaCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                runOnUiThread {
                    DaoMedia.setLiked(media.id)
                    media.user_has_liked = true
                    if(media.likes != null){
                        media.likes!!.count = media.likes!!.count + 1
                    }
                    updateLikeIcon(media)
                }
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(picture, getString(R.string.error_like), true)
            }
        })
    }

    companion object {
        val MEDIA_ID_EXTRA = "media_detail_id_extra"
    }
}
