package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 04/11/2017.
 */
open class ModelCaptionUser(
        var username: String = "",
        var full_name: String = "",
        var type: String = "",
        var id: String = ""
): RealmObject() {
}