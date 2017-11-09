package com.alessandrogaboardi.instatest.db.daos

import com.alessandrogaboardi.instatest.db.models.ModelCaption
import io.realm.RealmResults
import io.realm.Sort

/**
 * Created by alessandrogaboardi on 09/11/2017.
 */
object DaoComments : BaseDao() {
    fun saveComments(comments: MutableList<ModelCaption>) {
        executeTransaction {
            it.copyToRealmOrUpdate(comments)
        }
    }

    fun getMediaCommentsAsync(mediaId: String): RealmResults<ModelCaption> {
        return realm.where(ModelCaption::class.java).equalTo(ModelCaption.MEDIA_ID, mediaId).findAllSortedAsync(ModelCaption.CREATED_TIME, Sort.ASCENDING)
    }
}