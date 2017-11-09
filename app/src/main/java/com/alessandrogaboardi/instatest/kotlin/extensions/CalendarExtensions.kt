package com.alessandrogaboardi.instatest.kotlin.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by alessandrogaboardi on 09/11/2017.
 */
fun Calendar.resetTime(){
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.clear(Calendar.MINUTE)
    this.clear(Calendar.SECOND)
    this.clear(Calendar.MILLISECOND)
}

fun Calendar.format(format: String): String{
    val formatter = SimpleDateFormat(format, Locale.getDefault())

    return formatter.format(this.time)
}