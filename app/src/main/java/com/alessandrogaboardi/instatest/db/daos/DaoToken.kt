package com.alessandrogaboardi.instatest.db.daos

import com.alessandrogaboardi.instatest.db.models.ModelToken

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
object DaoToken : BaseDao() {
    fun saveToken(token: ModelToken) {
        executeTransaction {
            it.copyToRealmOrUpdate(token)
        }
    }

    fun getToken(): ModelToken? {
        val result = realm.where(ModelToken::class.java).findFirst()
        result?.let {
            return realm.copyFromRealm(it)
        } ?: return null
    }

    fun isEmpty(): Boolean {
        val result = realm.where(ModelToken::class.java).findFirst()
        result?.let {
            return false
        } ?: return true
    }
}