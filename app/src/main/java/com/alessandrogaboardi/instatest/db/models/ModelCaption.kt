package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by alessandro on 04/11/2017.
 */
open class ModelCaption(
        var created_time: Long = 0,
        var text: String = "",
        var from: ModelCaptionUser? = ModelCaptionUser(),
        @PrimaryKey
        var id: String = "",
        var mediaId: String = ""
) : RealmObject() {
    companion object {
        val MEDIA_ID = "mediaId"
        val ID = "id"
        val FROM = "from"
        val TEXT = "text"
        val CREATED_TIME = "created_time"
    }
}