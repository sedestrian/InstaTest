package com.alessandrogaboardi.instatest.kotlin.extensions

import android.support.design.widget.Snackbar
import android.view.View
import io.realm.Realm

/**
 * Created by alessandro on 05/11/2017.
 */
val realm: Realm get() = Realm.getDefaultInstance()

fun snack(view: View, text: String, long: Boolean?){
    val length = when (long){
        null -> Snackbar.LENGTH_INDEFINITE
        true -> Snackbar.LENGTH_LONG
        false -> Snackbar.LENGTH_SHORT
    }
    Snackbar.make(view, text, length).show()
}