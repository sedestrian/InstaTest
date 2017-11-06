package com.alessandrogaboardi.instatest.kotlin.extensions

import android.view.View

/**
 * Created by alessandro on 04/11/2017.
 */
fun View.setVisible(){
    this.visibility = View.VISIBLE
}

fun View.setInvisible(){
    this.visibility = View.INVISIBLE
}

fun View.setGone(){
    this.visibility = View.GONE
}