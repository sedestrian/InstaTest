package com.alessandrogaboardi.instatest.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.modules.GlideApp
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.profile_media_item.view.*

/**
 * Created by alessandro on 05/11/2017.
 */
class ProfileMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var picture: ImageView = itemView.picture

    fun bind(item: ModelMedia, click: ((item: ModelMedia, view: View) -> Unit)?){
        val pictureUri = item.images?.low_resolution?.url

        GlideApp.with(itemView.context).load(pictureUri).into(picture)

        itemView.setOnClickListener {
            click?.invoke(item, picture)
        }
    }
}