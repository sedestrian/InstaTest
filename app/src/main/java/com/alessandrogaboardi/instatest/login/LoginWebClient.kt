package com.alessandrogaboardi.instatest.login

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.db.models.ModelToken
import com.alessandrogaboardi.instatest.utils.QueryUtils

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
class LoginWebClient(
        var onLoginSuccess: (() -> Unit)? = null,
        var onLoginError: (() -> Unit)? = null
) : WebViewClient() {
    private val ACCESS_TOKEN = "access_token"

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        val redirectURL = view.context.getString(R.string.redirect_uri)
        //Todo: remove
        Log.v("LoginWebClient", "URL: " + redirectURL)

        val actualRedirectURI = Uri.parse(url)
        val redirectURI = Uri.parse(redirectURL)

        try {
            if (actualRedirectURI.scheme == redirectURI.scheme && actualRedirectURI.host == redirectURI.host) {
                //Fragment contains the access token i.e. http://your-redirect-uri#access_token=ACCESS-TOKEN
                val fragment = actualRedirectURI.fragment;

                if (fragment != null && !fragment.isEmpty()) {
                    val tokenHash = QueryUtils.splitQuery(fragment)
                    if (tokenHash.isNotEmpty() && tokenHash.containsKey("access_token")) {
                        val token = tokenHash[ACCESS_TOKEN]
                        token?.let {
                            saveToken(it)
                            onLoginSuccess?.invoke()
                        }
                    } else {
                        onLoginError?.invoke()
                    }
                } else {
                    onLoginError?.invoke()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToken(token: String) {
        DaoToken.saveToken(ModelToken(token))
    }
}