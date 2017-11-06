package com.alessandrogaboardi.instatest.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.adapters.holders.ProfileMediaViewHolder
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import io.realm.Realm

/**
 * Created by alessandro on 05/11/2017.
 */
class ProfileMediaAdapter(context: Context, val itemClickListener: ((item: ModelMedia, view: View) -> Unit)?) : RecyclerView.Adapter<ProfileMediaViewHolder>() {
    val inflater = LayoutInflater.from(context)
    var media: MutableList<ModelMedia> = mutableListOf()

    init {
        val mediaResults = DaoMedia.getUserMediaAsync()
        mediaResults.addChangeListener { data ->
            media = if (data.isManaged) {
                Realm.getDefaultInstance().copyFromRealm(data)
            } else {
                data
            }

            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ProfileMediaViewHolder, position: Int) {
        holder.bind(media[position], itemClickListener)
    }

    override fun getItemCount(): Int = media.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProfileMediaViewHolder {
        val view = inflater.inflate(R.layout.profile_media_item, parent, false)
        return ProfileMediaViewHolder(view)
    }
}