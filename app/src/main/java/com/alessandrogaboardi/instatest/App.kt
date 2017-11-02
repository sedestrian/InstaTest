package com.alessandrogaboardi.instatest

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration



/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm(){
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)
    }
}