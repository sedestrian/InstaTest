package com.alessandrogaboardi.instatest.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R
import com.alessandrogaboardi.instatest.adapters.holders.GalleryViewHolder
import com.alessandrogaboardi.instatest.db.daos.DaoMedia
import com.alessandrogaboardi.instatest.db.models.ModelMedia
import com.alessandrogaboardi.instatest.ws.ApiManager
import com.alessandrogaboardi.instatest.ws.callbacks.UserPicturesCallback
import io.realm.Realm
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * Created by alessandro on 04/11/2017.
 */
class GalleryAdapter(val context: Context, val dataDownloaded: (() -> Unit)?, val dataDownloadError: (() -> Unit)?, val onDetailRequested: ((item: ModelMedia, view: View) -> Unit)?) : RecyclerView.Adapter<GalleryViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pictures: MutableList<ModelMedia> = mutableListOf()

    init {
        downloadData()
    }

    fun setupLocal() {
        val mediaResult = DaoMedia.getUserMediaAsync()

        mediaResult.addChangeListener { result ->
            pictures = if (result.isManaged) {
                Realm.getDefaultInstance().copyFromRealm(result)
            } else {
                result
            }

            mediaResult.removeAllChangeListeners()

            notifyDataSetChanged()
        }
    }

    fun refresh() {
        downloadData()
    }

    private fun downloadData() {
        ApiManager.getSelfMedia(object : UserPicturesCallback {
            override fun onSuccess(call: Call?, response: Response?) {
                dataDownloaded?.invoke()
                setupLocal()
            }

            override fun onError(call: Call?, e: IOException?) {
                dataDownloadError?.invoke()
            }
        })
    }

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(pictures[position], onDetailRequested)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = inflater.inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)
    }
}