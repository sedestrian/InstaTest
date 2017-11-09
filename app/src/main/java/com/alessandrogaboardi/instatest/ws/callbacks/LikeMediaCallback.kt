package com.alessandrogaboardi.instatest.ws.callbacks

import com.alessandrogaboardi.instatest.kotlin.extensions.uiThread
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandro on 05/11/2017.
 */
interface LikeMediaCallback : ApiCallback {
    override fun onFailure(call: Call?, e: IOException?) {
        uiThread {
            onError(call, e)
        }
    }

    override fun onResponse(call: Call?, response: Response) {
        println(String(response.body()!!.bytes()))
        uiThread {
            onSuccess(call, response)
        }
    }
}