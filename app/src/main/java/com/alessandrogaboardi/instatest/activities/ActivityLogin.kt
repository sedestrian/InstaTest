package com.alessandrogaboardi.instatest.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.login.LoginWebClient
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login"

        val client = LoginWebClient({
            setResult(Activity.RESULT_OK)
            finish()
        },{
           setResult(Activity.RESULT_CANCELED)
        })
        webView.webViewClient = client
        webView.loadUrl(getString(R.string.login_url))
    }
}
