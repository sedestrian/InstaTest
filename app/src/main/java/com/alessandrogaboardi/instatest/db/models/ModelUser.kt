package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by luigi on 03/11/17.
 */
open class ModelUser(
        var id: String = "",
        var username: String = "",
        var profile_picture: String = "",
        var full_name: String = "",
        var bio: String = "",
        var website: String = "",
        var is_business: Boolean = false,
        var counts: ModelCounts? = null
): RealmObject() {
}