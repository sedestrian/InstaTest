package com.alessandrogaboardi.instatest.kotlin.extensions

import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
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

fun snack(view: View, @StringRes text: Int, long: Boolean?){
    val length = when (long){
        null -> Snackbar.LENGTH_INDEFINITE
        true -> Snackbar.LENGTH_LONG
        false -> Snackbar.LENGTH_SHORT
    }
    Snackbar.make(view, text, length).show()
}

fun snack(view: View, text: String, @StringRes actionText: Int, listener: (View) -> Unit){
    Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionText, listener)
            .show()
}

fun snack(view: View, @StringRes text: Int, @StringRes actionText: Int, listener: (View) -> Unit){
    Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionText, listener)
            .show()
}

fun uiThread(method: () -> Unit){
    Handler(Looper.getMainLooper()).post {
        method.invoke()
    }
}