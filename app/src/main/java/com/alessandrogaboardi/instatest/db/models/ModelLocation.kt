package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 03/11/2017.
 */
open class ModelLocation(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var id: String = "",
        var street_address: String = "",
        var name: String = ""
): RealmObject() {
}