package com.alessandrogaboardi.instatest.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.communicators.ActivityHomeCommunicator
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.fragments.FragmentHome
import com.alessandrogaboardi.instatest.kotlin.extensions.replaceMainFragment
import com.alessandrogaboardi.instatest.kotlin.extensions.setGone
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.gallery_item.view.*

class ActivityHome : AppCompatActivity(), ActivityHomeCommunicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.app_name)

        replaceMainFragment(FragmentHome.newInstance(), R.id.placeholder, false)

        profileIcon.setOnClickListener {
            openProfileActivity()
        }
    }

    private fun openProfileActivity() {
        val intent = Intent(this, ActivityUserProfile::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                profileIcon,
                getString(R.string.profile_icon_transition_name)
        )

        startActivity(intent, options.toBundle())
    }


    override fun onMediaDetailRequested(item: ModelMedia, view: View) {
        var intent = Intent(this, ActivityMediaDetail::class.java)
        intent.putExtra(ActivityMediaDetail.MEDIA_ID_EXTRA, item.id)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(view.picture, getString(R.string.transition_media_detail_image)),
                Pair(view.liked, getString(R.string.transition_media_detail_liked_icon)),
//                Pair(view.likes, getString(R.string.transition_media_detail_liked_text)),
                Pair(view.commentsIcon, getString(R.string.transition_media_detail_comment_icon)),
//                Pair(view.comments, getString(R.string.transition_media_detail_comment_text)),
                Pair(view.userPicture, getString(R.string.transition_media_detail_user_picture)),
                Pair(view.username, getString(R.string.transition_media_detail_username))
//                Pair(view.description, getString(R.string.transition_media_detail_description))
        )

        startActivity(intent, options.toBundle())
    }
}
