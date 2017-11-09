package com.alessandrogaboardi.instatest.db.daos

import com.alessandrogaboardi.instatest.db.models.ModelMedia
import io.realm.RealmResults

/**
 * Created by alessandro on 04/11/2017.
 */
object DaoMedia : BaseDao() {
    fun saveUserMedia(media: MutableList<ModelMedia>) {
        executeTransaction { it.copyToRealmOrUpdate(media) }
    }

    fun getUserMediaAsync(): RealmResults<ModelMedia> {
        return realm.where(ModelMedia::class.java).findAllAsync()
    }

    fun getByIdAsync(id: String): ModelMedia {
        return realm.where(ModelMedia::class.java).equalTo(ModelMedia.ID, id).findFirstAsync()
    }

    fun setLiked(media_id: String) {
        executeTransaction {
            val media = it.where(ModelMedia::class.java).equalTo(ModelMedia.ID, media_id).findFirst()
            if (media?.user_has_liked == false)
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count + 1
                }
            media?.user_has_liked = true
        }
    }

    fun setDisliked(media_id: String) {
        executeTransaction {
            val media = it.where(ModelMedia::class.java).equalTo(ModelMedia.ID, media_id).findFirst()
            if (media?.user_has_liked == true)
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count - 1
                }
            media?.user_has_liked = false
        }
    }
}