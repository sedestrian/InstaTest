package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 04/11/2017.
 */
open class ModelCount(
        var count: Int = 0
) : RealmObject() {
}