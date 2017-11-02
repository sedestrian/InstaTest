package com.alessandrogaboardi.instatest.db.daos

import io.realm.Realm

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
abstract class BaseDao {
    fun executeTransaction(transaction: (Realm) -> Unit) {
        Realm.getDefaultInstance().executeTransaction(transaction)
    }

    val realm: Realm get() = Realm.getDefaultInstance()
}