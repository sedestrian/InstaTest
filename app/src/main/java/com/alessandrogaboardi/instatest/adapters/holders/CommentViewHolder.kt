package com.alessandrogaboardi.instatest.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alessandrogaboardi.instatest.db.models.ModelCaption
import com.alessandrogaboardi.instatest.kotlin.extensions.format
import com.alessandrogaboardi.instatest.kotlin.extensions.resetTime
import kotlinx.android.synthetic.main.comment_layout.view.*
import java.util.*

/**
 * Created by alessandrogaboardi on 09/11/2017.
 */
class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val commentView = itemView.comment
    private val user = itemView.username
    private val time = itemView.time

    fun bind(comment: ModelCaption) {
        commentView.text = comment.text
        user.text = comment.from?.username
        val timeString = getTimeString(comment.created_time * 1000)
        time.text = timeString
    }

    private fun getTimeString(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date(millis)
        val today = Calendar.getInstance()
        today.resetTime()

        return if (calendar.before(today)) {
            calendar.format("MMM dd")
        } else {
            calendar.format("HH:mm")
        }
    }
}