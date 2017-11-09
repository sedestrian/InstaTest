package com.alessandrogaboardi.instatest.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.kotlin.extensions.isAvailable
import com.alessandrogaboardi.instatest.kotlin.extensions.realm
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_fullscreen_picture.*

class ActivityFullscreenPicture : AppCompatActivity() {
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_picture)

        supportPostponeEnterTransition()

        intent?.let {
            id = it.getStringExtra(PICTURE_ID_EXTRA)
            if (id.isNotEmpty())
                loadPicture()
        }

    }

    private fun loadPicture() {
        val pictureResult = DaoMedia.getByIdAsync(id)
        pictureResult.addChangeListener<ModelMedia> { pictureObject ->
            if (isAvailable) {
                val pictureData = if (pictureObject.isManaged)
                    realm.copyFromRealm(pictureObject)
                else
                    pictureObject

                val pictureUrl = pictureData.images?.standard_resolution?.url

                GlideApp.with(this).load(pictureUrl)
                        .dontAnimate()
                        .listener(object: RequestListener<Drawable>{
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                supportStartPostponedEnterTransition()
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                supportStartPostponedEnterTransition()
                                return false
                            }
                        })
                        .into(picture)
            }
        }
    }

    companion object {
        val PICTURE_ID_EXTRA = "picture_id_extra_fullscreen"
    }
}
