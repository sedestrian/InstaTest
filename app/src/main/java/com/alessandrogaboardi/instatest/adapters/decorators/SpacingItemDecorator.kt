package com.alessandrogaboardi.instatest.adapters.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by alessandro on 04/11/2017.
 */
class SpacingItemDecorator(private val verticalSpacing: Int, private val horizontalSpacing: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpacing/2
        outRect.top = verticalSpacing/2
        outRect.left = horizontalSpacing/2
        outRect.right = horizontalSpacing/2
    }
}