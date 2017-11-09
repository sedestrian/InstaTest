package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by luigi on 03/11/17.
 */
open class ModelCounts(
        var media: Int = 0,
        var follows: Int = 0,
        var followed_by: Int = 0
): RealmObject() {
}