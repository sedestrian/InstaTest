package com.alessandrogaboardi.instatest.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.kotlin.extensions.setGone
import com.alessandrogaboardi.instatest.kotlin.extensions.setVisible
import com.alessandrogaboardi.instatest.kotlin.extensions.snack
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.LikeMediaCallback
import kotlinx.android.synthetic.main.gallery_item.view.*
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandro on 04/11/2017.
 */
class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var picture = itemView.picture
    var liked = itemView.liked
    var likes = itemView.likes
    var comments = itemView.comments
    var username = itemView.username
    var userPicture = itemView.userPicture
    var description = itemView.description

    fun bind(item: ModelMedia, onDetailRequested: ((item: ModelMedia, mainView: View) -> Unit)?) {
        val pict = item.images?.low_resolution
        val userPict = item.user?.profile_picture
        val username = item.user?.username
        val likes = item.likes?.count
        val comments = item.comments?.count
        val description = item.caption?.text

        this.username.text = username
        this.likes.text = likes.toString()
        this.comments.text = comments.toString()
        this.description.text = description

        if (this.description.text.isEmpty()) this.description.setGone()
        else this.description.setVisible()

        GlideApp.with(itemView.context).load(pict?.url).into(this.picture)
        GlideApp.with(itemView.context).load(userPict).circleCrop().into(this.userPicture)

        updateLikeIcon(item)

        this.liked.setOnClickListener {
            if (item.user_has_liked) {
                dislikeMedia(item)
            } else {
                likeMedia(item)
            }
        }

        this.itemView.setOnClickListener {
            onDetailRequested?.invoke(item, itemView)
        }
    }

    private fun updateLikeIcon(item: ModelMedia) {
        if (item.user_has_liked) {
            this.liked.setImageResource(R.drawable.full_heart)
        } else {
            this.liked.setImageResource(R.drawable.empty_heart)
        }

        println(item.likes?.count)
        this.likes.text = item.likes?.count.toString()
    }

    private fun dislikeMedia(media: ModelMedia) {
        ApiManager.dislikeMedia(media.id, object : LikeMediaCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                DaoMedia.setDisliked(media.id)
                media.user_has_liked = false
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count - 1
                }
                updateLikeIcon(media)
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(this@GalleryViewHolder.picture, itemView.context.getString(R.string.error_dislike), true)
            }
        })
    }

    private fun likeMedia(media: ModelMedia) {
        ApiManager.likeMedia(media.id, object : LikeMediaCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                DaoMedia.setLiked(media.id)
                media.user_has_liked = true
                if (media.likes != null) {
                    media.likes!!.count = media.likes!!.count + 1
                }
                updateLikeIcon(media)
            }

            override fun onError(call: Call?, e: IOException?) {
                snack(this@GalleryViewHolder.picture, itemView.context.getString(R.string.error_like), true)
            }
        })
    }
}