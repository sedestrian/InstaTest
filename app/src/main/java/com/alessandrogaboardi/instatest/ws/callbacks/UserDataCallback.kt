package com.alessandrogaboardi.instatest.ws.callbacks

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
interface UserDataCallback: Callback {
    override fun onFailure(call: Call?, e: IOException?) {
        //CODE HERE
        onError(call, e)
    }

    override fun onResponse(call: Call?, response: Response){
        println(String(response.body()?.bytes() ?: byteArrayOf()))
        onSuccess(call, response)
    }

    fun onSuccess(call: Call?, response: Response?)
    fun onError(call: Call?, e: IOException?)
}