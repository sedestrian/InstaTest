package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmObject

/**
 * Created by alessandro on 03/11/2017.
 */
open class ModelImages(
        var thumbnail: ModelMediaResolution? = ModelMediaResolution(),
        var low_resolution: ModelMediaResolution? = ModelMediaResolution(),
        var standard_resolution: ModelMediaResolution? = ModelMediaResolution()
): RealmObject() {
}