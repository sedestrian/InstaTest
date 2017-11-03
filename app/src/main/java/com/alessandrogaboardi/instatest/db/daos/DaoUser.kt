package com.alessandrogaboardi.instatest.db.daos

import com.alessandrogaboardi.instatest.db.models.ModelUser

/**
 * Created by luigi on 03/11/17.
 */
object DaoUser : BaseDao() {
    fun saveUser(user: ModelUser) {
        executeTransaction {
            it.copyToRealmOrUpdate(user)
        }
    }

    fun getUser(): ModelUser? {
        val result = realm.where(ModelUser::class.java).findFirst()
        result?.let {
            return realm.copyFromRealm(it)
        } ?: return null
    }
}