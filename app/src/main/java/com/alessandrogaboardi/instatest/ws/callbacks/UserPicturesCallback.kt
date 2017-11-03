package com.alessandrogaboardi.instatest.ws.callbacks

import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
interface UserPicturesCallback: ApiCallback {
    override fun onFailure(call: Call?, e: IOException?) {
        onError(call, e)
    }

    override fun onResponse(call: Call?, response: Response) {

        

        onSuccess(call, response)
    }
}