package com.alessandrogaboardi.instatest.views

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.kotlin.extensions.setGone
import com.alessandrogaboardi.instatest.kotlin.extensions.setVisible
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.video_view.view.*


/**
 * Created by alessandro on 08/11/2017.
 */
class AutoVideoView : FrameLayout {
    private var HIDE_BUTTON_DELAY: Long = 2000
    private var ANIMATION_DURATION: Long = 500
    private var previewImage: String? = null

    private var onPreviewLoaded: ((imageView: ImageView) -> Unit)? = null
    private var onPreviewLoadError: (() -> Unit)? = null
    private var videoUri: Uri? = null

    private var playing: Boolean = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.video_view, this)
        setupViews()
    }

    private fun setupViews() {
        setupPreviewImage()
        setupVideoView()
        setupControls()
    }

    private fun setupControls() {
        autoVideoViewPlay.setImageResource(R.drawable.play)
        autoVideoViewPlay.setOnClickListener {
            if (playing) {
                setPaused()
            } else {
                setPlaying()
            }
        }
    }

    private fun setupVideoView() {
        autoVideoViewVideo.setVideoURI(videoUri)
        autoVideoViewVideo.requestLayout()
        autoVideoViewVideo.setOnCompletionListener {
            playing = false
            autoVideoViewPlay.setImageResource(R.drawable.play)
        }
        autoVideoViewVideo.setOnTouchListener { view, motionEvent ->
            showButton()
            true
        }
    }

    private fun setupPreviewImage() {
        GlideApp.with(context)
                .load(previewImage)
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        onPreviewLoadError?.invoke()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        onPreviewLoaded?.invoke(autoVideoViewPicture)
                        return false
                    }
                })
                .into(autoVideoViewPicture)
    }

    private fun setPlaying() {
        playing = true
        if (autoVideoViewPicture.visibility == View.VISIBLE)
            autoVideoViewPicture.setGone()
        autoVideoViewVideo.start()
        autoVideoViewPlay.setImageResource(R.drawable.pause)
        Handler().postDelayed({
            hideButton()
        }, HIDE_BUTTON_DELAY)
    }

    private fun setPaused() {
        playing = false
        autoVideoViewVideo.pause()
        autoVideoViewPlay.setImageResource(R.drawable.play)
    }

    private fun hideButton() {
        if (playing) {
            autoVideoViewPlay.isClickable = false
            autoVideoViewPlay.animate()
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {
                            this@AutoVideoView.autoVideoViewPlay.setGone()
                        }

                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationStart(p0: Animator?) {}
                    })
        }
    }

    private fun showButton() {
        autoVideoViewPlay.animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {
                        this@AutoVideoView.autoVideoViewPlay.isClickable = true
                        Handler().postDelayed({
                            hideButton()
                        }, (HIDE_BUTTON_DELAY).toLong())
                    }

                    override fun onAnimationCancel(p0: Animator?) {}
                    override fun onAnimationStart(p0: Animator?) {
                        this@AutoVideoView.autoVideoViewPlay.setVisible()
                    }
                })
    }

    fun shareVideo() {
        videoUri?.let {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.sharing_video_subject))
            i.putExtra(Intent.EXTRA_TEXT, it.toString())
            context.startActivity(Intent.createChooser(i, context.getString(R.string.sharing_video_subject)))
        }
    }

    fun setPreviewImage(url: String?) {
        previewImage = url
        setupPreviewImage()
    }

    fun setOnPreviewLoaded(listener: ((imageView: ImageView) -> Unit)?) {
        onPreviewLoaded = listener
    }

    fun setOnPreviewLoadError(listener: (() -> Unit)?) {
        onPreviewLoadError = listener
    }

    fun setVideoURL(url: String?) {
        val uri = Uri.parse(url)
        setVideoURI(uri)
    }

    fun setVideoURI(uri: Uri) {
        videoUri = uri
        setupVideoView()
    }
}