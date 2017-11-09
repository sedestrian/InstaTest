package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
open class ModelString(
        var string: String = ""
) : RealmObject() {
}