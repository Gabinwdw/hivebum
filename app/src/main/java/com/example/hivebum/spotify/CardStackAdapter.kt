package com.example.hivebum.spotify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hivebum.R

class CardStackAdapter(
        private var albums: List<AlbumData> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.card_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albums[position]
        //holder.name.text = "${album.genre}"
        Glide.with(holder.image).load(album.url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setSpots(albums: List<AlbumData>) {
        this.albums = albums
    }

    fun getSpots(): List<AlbumData> {
        return albums
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //var name: TextView = view.findViewById(R.id.genreNameText)
        var image: ImageView = view.findViewById(R.id.item_image)
    }

}
