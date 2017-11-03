package com.alessandrogaboardi.instatest.ws

import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.ws.callbacks.UserDataCallback
import com.alessandrogaboardi.instatest.ws.callbacks.UserPicturesCallback
import okhttp3.*
import java.io.IOException

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
object ApiManager {
    var client: OkHttpClient? = null

    fun getUserData(callback: UserDataCallback){
        val request = Request.Builder()
                .url(ApiConstants.GET_USER_INFO + DaoToken.getToken()?.token)
                .build()

        addToRequestQueue(request, callback)
    }

    fun getSelfMedia(callback: UserPicturesCallback){
        val request = Request.Builder()
                .url(ApiConstants.GET_SELF_MEDIA + DaoToken.getToken()?.token)
                .build()

        addToRequestQueue(request, callback)
    }

    fun addToRequestQueue(request: Request, callback: Callback){
        setupClient()

        client!!.newCall(request).enqueue(callback)
    }

    private fun setupClient(){
        if(client == null){
            client = OkHttpClient()
        }
    }
}