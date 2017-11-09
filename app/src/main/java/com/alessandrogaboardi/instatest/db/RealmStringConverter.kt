package com.alessandrogaboardi.instatest.db

import com.alessandrogaboardi.instatest.db.models.ModelString
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import io.realm.RealmList
import java.lang.reflect.Type

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
class RealmStringConverter : JsonSerializer<RealmList<ModelString>>, JsonDeserializer<RealmList<ModelString>> {

    override fun serialize(src: RealmList<ModelString>, typeOfSrc: Type,
                           context: JsonSerializationContext): JsonElement {

        val ja = JsonArray()
        for (string in src) {
            ja.add(context.serialize(string))
        }
        return ja
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): RealmList<ModelString> {

        val strings = RealmList<ModelString>()
        val ja = json.asJsonArray
        ja.mapTo(strings) { ModelString(it.asString) }
        return strings
    }

    companion object {

        val gson: Gson
            get() = GsonBuilder()
                    .registerTypeAdapter(object : TypeToken<RealmList<ModelString>>() {

                    }.type,
                            RealmStringConverter())
                    .create()
    }
}