package com.alessandrogaboardi.instatest.ws.callbacks

import com.alessandrogaboardi.instatest.db.daos.DaoComments
import com.alessandrogaboardi.instatest.db.models.ModelCaption
import com.alessandrogaboardi.instatest.kotlin.extensions.uiThread
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Created by alessandrogaboardi on 09/11/2017.
 */
interface GetCommentsCallback : ApiCallback {
    var callbackMediaId: String set

    override fun onFailure(call: Call?, e: IOException?) {
        //CODE HERE
        uiThread {
            onError(call, e)
        }
    }

    override fun onResponse(call: Call?, response: Response) {
        val responseString = String(response.body()!!.bytes())
        if(responseString.isNotEmpty()) {
            val jsonObject = JSONObject(responseString)
            if(jsonObject.has(DATA_FIELD)) {
                val commentsJson = jsonObject.getJSONArray(DATA_FIELD)
                val type = object : TypeToken<MutableList<ModelCaption>>() {}.type
                val comments = Gson().fromJson<MutableList<ModelCaption>>(commentsJson.toString(), type)
                comments.forEach { it.mediaId = callbackMediaId }

                DaoComments.saveComments(comments)

                uiThread {
                    onSuccess(call, response)
                }
            }else{
                uiThread {
                    onError(call, null)
                }
            }
        }else{
            uiThread {
                onError(call, null)
            }
        }
    }
}