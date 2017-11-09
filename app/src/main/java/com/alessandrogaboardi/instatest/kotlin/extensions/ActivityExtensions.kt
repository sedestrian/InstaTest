package com.alessandrogaboardi.instatest.kotlin.extensions

import android.app.Activity
import android.os.Build
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by alessandro on 05/11/2017.
 */
val Activity.isAvailable: Boolean
    get() {
        return if (Build.VERSION.SDK_INT >= 17)
            !this.isFinishing && !this.isDestroyed
        else
            !this.isFinishing
    }

fun AppCompatActivity.replaceMainFragment(fragment: Fragment, @IdRes layout_id: Int, add_to_backstack: Boolean) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(layout_id, fragment)
    if (add_to_backstack)
        transaction.addToBackStack(fragment::class.java.simpleName)
    transaction.commit()
}

fun AppCompatActivity.replaceMainFragment(fragment: Fragment, @IdRes layout_id: Int, add_to_backstack: Boolean, vararg sharedElements: Pair<View, String>) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(layout_id, fragment)
    if(add_to_backstack)
        transaction.addToBackStack(fragment::class.java.simpleName)
    sharedElements.forEach {
        transaction.addSharedElement(it.first, it.second)
    }
    transaction.commit()
}