package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
open class ModelMedia(
        var id: String = "",
        var user: ModelUser? = null,
        var images: ModelImages? = null,
        var created_time: Date? = null,
        var caption: String? = null,
        var user_has_liked: Boolean = false,
        var likes: ModelCount? = null,
        var tags: RealmList<ModelString> = RealmList(),
        var filter: String = "",
        var comments: ModelComments? = null,
        var type: String = "",
        var link: String = "",
        var location: ModelLocation? = null,
        var videos: ModelVideos? = null
        ): RealmObject(){
}