package com.alessandrogaboardi.instatest.fragments

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.db.daos.DaoUser
import com.alessandrogaboardi.instatest.kotlin.extensions.getLoginIntent
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.UserDataCallback
import kotlinx.android.synthetic.main.fragment_activity_home.*
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentHome : Fragment() {

    val ACTIVITY_CODE = 2876

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        checkToken()
    }

    private fun checkToken() {
        if (DaoToken.isEmpty()) {
            login()
        } else {
            setupUserData()
        }
    }

    private fun login() {
        startActivityForResult(activity.getLoginIntent(), ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setupUserData()
            } else {

            }
        }
    }

    private fun setupUserData() {
        ApiManager.getUserData(object : UserDataCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                val user = DaoUser.getUser()
                activity.runOnUiThread {
                    username.text = user?.username
                }
            }

            override fun onError(call: Call?, e: IOException?) {

            }

        })
    }
}
