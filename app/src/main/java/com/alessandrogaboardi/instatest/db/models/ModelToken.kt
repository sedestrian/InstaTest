package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
open class ModelToken(
        @PrimaryKey
        var token: String = ""
): RealmObject() {
}