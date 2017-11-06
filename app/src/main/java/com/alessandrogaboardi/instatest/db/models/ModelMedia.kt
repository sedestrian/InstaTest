package com.alessandrogaboardi.instatest.db.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by alessandrogaboardi on 03/11/2017.
 */
open class ModelMedia(
        @PrimaryKey
        var id: String = "",
        var user: ModelUser? = ModelUser(),
        var images: ModelImages? = ModelImages(),
        var created_time: String = "",
        var caption: ModelCaption? = ModelCaption(),
        var user_has_liked: Boolean = false,
        var likes: ModelCount? = ModelCount(),
        var tags: RealmList<ModelString> = RealmList(),
        var filter: String = "",
        var comments: ModelCount? = ModelCount(),
        var type: String = "",
        var link: String = "",
        var location: ModelLocation? = ModelLocation(),
        var videos: ModelVideos? = ModelVideos()
) : RealmObject() {
    companion object {
        val ID = "id"
    }
}