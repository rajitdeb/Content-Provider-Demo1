package com.rajit.contentprovidersample_musicplayerapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rajit.contentprovidersample_musicplayerapp.databinding.SongItemviewBinding

class SongsRvAdapter: RecyclerView.Adapter<SongsRvAdapter.SongsRvViewHolder>() {

    private var songsList = mutableListOf<Song>()

    inner class SongsRvViewHolder(private val customView: SongItemviewBinding):
        RecyclerView.ViewHolder(customView.root) {

            fun bind(currentSong: Song) {
                customView.apply {
                    titleTv.text = currentSong.title
                    durationTv.text = MediaUtil.convertLongToDuration(currentSong.duration)
                    artistTv.text = currentSong.artist

                    albumArtIv.setImageResource(R.drawable.error_album_art)

                    albumArtIv.load(currentSong.albumArt) {
                        placeholder(R.drawable.error_album_art)
                        error(R.drawable.error_album_art)
                    }
                }
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsRvViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = SongItemviewBinding.inflate(inflater, parent, false)
        return SongsRvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: SongsRvViewHolder, position: Int) {
        val currentSong = songsList[position]

        Log.i("SongRvAdapter", "onBindViewHolder: $currentSong")

        holder.bind(currentSong)
    }

    fun setList(newSongsList: ArrayList<Song>) {
        val util = SongsDiffUtil(songsList, newSongsList)
        val diffUtilResult = DiffUtil.calculateDiff(util)
        songsList = newSongsList
        diffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

}