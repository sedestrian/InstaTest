package com.alessandrogaboardi.instatest.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.adapters.holders.CommentViewHolder
import com.alessandrogaboardi.instatest.db.daos.DaoComments
import com.alessandrogaboardi.instatest.db.models.ModelCaption
import com.alessandrogaboardi.instatest.kotlin.extensions.realm
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.GetCommentsCallback
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandrogaboardi on 09/11/2017.
 */
class CommentsAdapter(val mediaId: String, val loadError: (() -> Unit)?, val loaded: (() -> Unit)?, val loadStarted: (() -> Unit)?) : RecyclerView.Adapter<CommentViewHolder>() {
    var comments: MutableList<ModelCaption> = mutableListOf()

    init {
        loadStarted?.invoke()
        ApiManager.getMediaComments(mediaId, object : GetCommentsCallback {
            override var callbackMediaId: String
                get() = mediaId
                set(value) {}

            override fun onSuccess(call: Call?, response: Response?) {
                setupComments()
            }

            override fun onError(call: Call?, e: IOException?) {
                loadError?.invoke()
            }
        })
    }

    private fun setupComments() {
        val commentResults = DaoComments.getMediaCommentsAsync(mediaId)
        commentResults.addChangeListener { commentObject ->
            comments = if (commentObject.isManaged)
                realm.copyFromRealm(commentObject)
            else
                commentObject

            notifyDataSetChanged()
            loaded?.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size
}