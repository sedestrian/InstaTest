package com.alessandrogaboardi.instatest.kotlin.extensions

import android.content.Context
import android.content.Intent
import com.alessandrogaboardi.instatest.activities.ActivityLogin

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
fun Context.getLoginIntent(): Intent {
    return Intent(this, ActivityLogin::class.java)
}