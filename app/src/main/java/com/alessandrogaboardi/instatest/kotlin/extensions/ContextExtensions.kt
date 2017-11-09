package com.alessandrogaboardi.instatest.kotlin.extensions

import android.content.Context
import android.content.Intent
import com.alessandrogaboardi.instatest.activities.ActivityLogin

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
fun Context.getLoginIntent(): Intent {
    val intent =  Intent(this, ActivityLogin::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    return intent
}