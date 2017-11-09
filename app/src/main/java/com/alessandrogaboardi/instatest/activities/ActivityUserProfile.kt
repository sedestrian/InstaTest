package com.alessandrogaboardi.instatest.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.adapters.ProfileMediaAdapter
import com.alessandrogaboardi.instatest.db.daos.DaoUser
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.db.models.ModelUser
import com.alessandrogaboardi.instatest.kotlin.extensions.isAvailable
import com.alessandrogaboardi.instatest.kotlin.extensions.setGone
import com.alessandrogaboardi.instatest.modules.GlideApp
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_user_profile.*

class ActivityUserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        close.setOnClickListener {
            onBackPressed()
        }

        setupMedia()
        setupUser()
    }

    private fun setupUser() {
        val userData = DaoUser.getUserAsync()
        userData.addChangeListener<ModelUser> { userObject ->
            if(isAvailable) {
                val user = if (userObject.isManaged) {
                    Realm.getDefaultInstance().copyFromRealm(userObject)
                } else {
                    userObject
                }

                user.let {
                    val usr = it.username
                    val name = it.full_name
                    val site = it.website
                    val biography = it.bio
                    val posts = it.counts?.media
                    val followers = it.counts?.followed_by
                    val follows = it.counts?.follows

                    fullName.text = name
                    username.text = usr
                    website.text = site
                    bio.text = biography

                    if (website.text.isEmpty()) website.setGone()
                    if (bio.text.isEmpty()) bio.setGone()

                    postNumber.text = posts.toString()
                    followerNumber.text = followers.toString()
                    followingNumber.text = follows.toString()

                    GlideApp.with(this).load(it.profile_picture).circleCrop().into(userPicture)

                    userPicture.setOnClickListener {
                        Snackbar.make(coordinator, "That's you! I know, scary right?!", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupMedia() {
        pictures.layoutManager = GridLayoutManager(this, 3)
        pictures.adapter = ProfileMediaAdapter(this, { item, view ->
            openPictureFullscreen(item, view)
        })
    }

    private fun openPictureFullscreen(item: ModelMedia, view: View) {
        val intent = Intent(this, ActivityFullscreenPicture::class.java)
        intent.putExtra(ActivityFullscreenPicture.PICTURE_ID_EXTRA, item.id)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                getString(R.string.fullscreen_element_name)
        )

        startActivity(intent, options.toBundle())
    }
}
