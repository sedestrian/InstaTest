package com.alessandrogaboardi.instatest.ws.callbacks

import com.alessandrogaboardi.instatest.db.RealmStringConverter
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
interface UserPicturesCallback : ApiCallback {
    override fun onFailure(call: Call?, e: IOException?) {
        onError(call, e)
    }

    override fun onResponse(call: Call?, response: Response) {

        response.body()?.let {
            val jsonObject = JSONObject(String(it.bytes()))
            val mediaJson = jsonObject.getJSONArray(DATA_FIELD)

            val type = object : TypeToken<ArrayList<ModelMedia>>() {}.type
            val media = RealmStringConverter.gson.fromJson<ArrayList<ModelMedia>>(mediaJson.toString(), type)

            DaoMedia.saveUserMedia(media)

            onSuccess(call, response)
        } ?: onError(call, null)

    }
}