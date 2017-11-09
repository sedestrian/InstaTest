package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 03/11/2017.
 */
open class ModelMediaResolution(
        var width: Int = 0,
        var height: Int = 0,
        var url: String = ""
): RealmObject() {
}