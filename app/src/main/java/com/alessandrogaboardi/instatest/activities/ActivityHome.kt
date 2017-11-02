package com.alessandrogaboardi.instatest.activities

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.kotlin.extensions.getLoginIntent
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.UserDataCallback

import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        checkToken()
    }

    private fun checkToken(){
        if(DaoToken.isEmpty()){
            login()
        }else{
            setupUserData()
        }
    }

    private fun login(){
        startActivity(getLoginIntent())
    }

    private fun setupUserData(){
        ApiManager.getUserData(object: UserDataCallback{
            override fun onSuccess(call: Call?, response: Response?) {

            }

            override fun onError(call: Call?, e: IOException?) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
