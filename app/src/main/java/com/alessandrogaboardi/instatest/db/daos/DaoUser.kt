package com.alessandrogaboardi.instatest.db.daos

import com.alessandrogaboardi.instatest.db.models.ModelUser

/**
 * Created by luigi on 03/11/17.
 */
object DaoUser : BaseDao() {
    fun saveUser(user: ModelUser) {
        executeTransaction {
            it.delete(ModelUser::class.java)
            it.copyToRealm(user)
        }
    }

    fun getUserAsync(): ModelUser {
        return realm.where(ModelUser::class.java).findFirstAsync()
    }
}