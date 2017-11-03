package com.alessandrogaboardi.instatest.ws.callbacks

import com.alessandrogaboardi.instatest.db.daos.DaoUser
import com.alessandrogaboardi.instatest.db.models.ModelUser
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
interface ApiCallback: Callback {
    override fun onFailure(call: Call?, e: IOException?)
    override fun onResponse(call: Call?, response: Response)

    fun onSuccess(call: Call?, response: Response?)
    fun onError(call: Call?, e: IOException?)
}