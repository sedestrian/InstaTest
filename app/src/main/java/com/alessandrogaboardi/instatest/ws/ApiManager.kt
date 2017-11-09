package com.alessandrogaboardi.instatest.ws

import com.alessandrogaboardi.instatest.db.daos.DaoToken
import com.alessandrogaboardi.instatest.ws.callbacks.GetCommentsCallback
import com.alessandrogaboardi.instatest.ws.callbacks.LikeMediaCallback
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

    fun getMediaComments(media_id: String, callback: GetCommentsCallback){
        val url = String.format(ApiConstants.GET_MEDIA_COMMENTS, media_id) + DaoToken.getToken()?.token
        println(url)
        val request = Request.Builder()
                .url(url)
                .build()

        addToRequestQueue(request, callback)
    }

    fun likeMedia(media_id: String, callback: LikeMediaCallback){
        val url = String.format(ApiConstants.LIKE_PICTURE, media_id) + DaoToken.getToken()?.token
        println(url)
        val body = RequestBody.create(null, String())

        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        addToRequestQueue(request, callback)
    }

    fun dislikeMedia(media_id: String, callback: LikeMediaCallback){
        val url = String.format(ApiConstants.DISLIKE_PICTURE, media_id) + DaoToken.getToken()?.token
        println(url)

        val request = Request.Builder()
                .url(url)
                .delete()
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