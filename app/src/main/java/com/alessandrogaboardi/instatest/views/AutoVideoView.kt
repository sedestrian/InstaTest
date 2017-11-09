package com.alessandrogaboardi.instatest.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView

/**
 * Created by alessandro on 08/11/2017.
 */
class AutoVideoView : VideoView {
    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    constructor(context: Context): super(context)

    fun setVideoSize(width: Int, height: Int) {
        mVideoWidth = width
        mVideoHeight = height
    }

    
}