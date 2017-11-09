package com.alessandrogaboardi.instatest.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.activities.ActivityUserProfile
import com.alessandrogaboardi.instatest.adapters.GalleryAdapter
import com.alessandrogaboardi.instatest.adapters.decorators.SpacingItemDecorator
import com.alessandrogaboardi.instatest.communicators.ActivityHomeCommunicator
import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.db.daos.DaoUser
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.db.models.ModelUser
import com.alessandrogaboardi.instatest.kotlin.extensions.*
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.UserDataCallback
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_activity_home.*
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.support.v4.dip
import java.io.IOException

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentHome : Fragment() {
    private val ACTIVITY_CODE = 2876
    private var adapter: GalleryAdapter? = null
    private var communicator: ActivityHomeCommunicator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCommunicators()
        refresh.setOnRefreshListener {
            adapter?.refresh()
        }
        checkToken()
        retry.setOnClickListener {
            refresh.isRefreshing = true
            adapter?.refresh()
        }
    }

    private fun initCommunicators() {
        if (activity is ActivityHomeCommunicator)
            communicator = activity as ActivityHomeCommunicator
    }

    private fun checkToken() {
        if (DaoToken.isEmpty()) {
            login()
        } else {
            setupData()
        }
    }

    private fun login() {
        startActivityForResult(activity?.getLoginIntent(), ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setupData()
            } else {

            }
        }
    }

    private fun setupData() {
        setupUserData()
        setupMedia()
    }

    fun updateItem(media: ModelMedia?) {
        media?.let {
            adapter?.updateItem(media)
        }
    }

    private fun setupMedia() {
        val divider = SpacingItemDecorator(dip(8), dip(8))
        media.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        media?.addItemDecoration(divider)
        adapter = GalleryAdapter(activity!!, {
            refresh?.isRefreshing = false
            if (media?.visibility == View.GONE) {
                media?.setVisible()
            }
            noPicturesLayout?.setGone()
        }, {
            if (adapter?.itemCount == 0)
                noPicturesLayout?.setVisible()
            else
                snack(refresh, R.string.error_occurred, R.string.retry, { adapter?.refresh() })
            refresh.isRefreshing = false
        }, { item, view ->
            communicator?.onMediaDetailRequested(item, view)
        })
        media?.adapter = adapter
    }

    private fun setupUserData() {
        activity?.profileProgress?.setVisible()
        activity?.profileIcon?.setGone()
        ApiManager.getUserData(object : UserDataCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                activity?.profileProgress?.setGone()
                activity?.profileIcon?.setVisible()
                val userData = DaoUser.getUserAsync()

                userData.addChangeListener<ModelUser> { userObject ->
                    val user = if (userObject.isManaged)
                        realm.copyFromRealm(userObject)
                    else
                        userObject

                    userData.removeAllChangeListeners()

                    if (activity != null) {
                        if (user.profile_picture.isNotEmpty())
                            GlideApp.with(activity).load(user.profile_picture).circleCrop().into(activity?.profileIcon)
                        else
                            GlideApp.with(activity).load(R.drawable.no_account).circleCrop().into(activity?.profileIcon)
                    }

                    activity?.profileIcon?.setOnClickListener {
                        openProfileActivity()
                    }
                }
            }

            override fun onError(call: Call?, e: IOException?) {
                GlideApp.with(activity).load(R.drawable.no_account).circleCrop().into(activity?.profileIcon)
                activity?.profileProgress?.setGone()
                activity?.profileIcon?.setVisible()
                activity?.profileIcon?.setOnClickListener {
                    setupUserData()
                }
            }
        })
    }

    private fun openProfileActivity() {
        val intent = Intent(activity, ActivityUserProfile::class.java)
        activity?.profileIcon?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity as Activity,
                    it,
                    getString(R.string.profile_icon_transition_name)
            )

            startActivity(intent, options.toBundle())
        }
    }

    companion object {
        fun newInstance(): FragmentHome = FragmentHome()
    }
}
