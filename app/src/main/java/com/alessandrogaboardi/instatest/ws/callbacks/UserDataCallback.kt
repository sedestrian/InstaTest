package com.alessandrogaboardi.instatest.ws.callbacks

import com.alessandrogaboardi.instatest.db.daos.DaoUser
import com.alessandrogaboardi.instatest.db.models.ModelUser
import com.alessandrogaboardi.instatest.kotlin.extensions.uiThread
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
interface UserDataCallback : ApiCallback {
    override fun onFailure(call: Call?, e: IOException?) {
        //CODE HERE
        uiThread {
            onError(call, e)
        }
    }

    override fun onResponse(call: Call?, response: Response) {
        val jsonObject = JSONObject(String(response.body()!!.bytes()))
        val userJson = jsonObject.getJSONObject(DATA_FIELD)
        val user = Gson().fromJson<ModelUser>(userJson.toString(), ModelUser::class.java)

        DaoUser.saveUser(user)

        uiThread {
            onSuccess(call, response)
        }
    }
}