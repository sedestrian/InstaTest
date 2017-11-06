package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 04/11/2017.
 */
open class ModelCaption(
        var created_time: String = "",
        var text: String = "",
        var from: ModelCaptionUser? = ModelCaptionUser(),
        var id: String = ""
): RealmObject() {
}