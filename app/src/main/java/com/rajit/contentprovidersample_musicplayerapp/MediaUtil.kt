package com.rajit.contentprovidersample_musicplayerapp

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.util.Locale

object MediaUtil {

    fun getAllAudioFiles(context: Context): ArrayList<Song> {

        val songList = ArrayList<Song>()
        val contentResolver = context.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf<String>(
            MediaConstants.SONG_ID,
            MediaConstants.SONG_TITLE,
            MediaConstants.SONG_ARTIST,
            MediaConstants.SONG_URI_PATH,
            MediaConstants.SONG_DURATION,
            MediaConstants.SONG_ALBUM_ID
        )

        // queries the entire storage directory and handover the results to content resolver
        // in the form of a cursor
        val songCursor = contentResolver.query(
            songUri,
            projection,
            null,
            null,
            null
        )

        if (songCursor != null) {

            while (songCursor.moveToNext()) {

                val song = getSong(songCursor)
                songList.add(song)
            }

            songCursor.close()
        }

        // For Android 10 and above, also query the Downloads folder separately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val downloadsSongUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL)

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

            val downloadsCursor = contentResolver.query(
                downloadsSongUri,
                projection,
                selection,
                null,
                null
            )

            if (downloadsCursor != null) {

                while (downloadsCursor.moveToNext()) {
                    val song = getSong(downloadsCursor)
                    songList.add(song)
                }

                downloadsCursor.close()
            }
        }

        return songList

    }

    private fun getSong(songCursor: Cursor): Song {

        songCursor.apply {

            val id: Long = getLong(getColumnIndexOrThrow(MediaConstants.SONG_ID))

            val title: String =
                getString(getColumnIndexOrThrow(MediaConstants.SONG_TITLE))

            val artist: String =
                getString(getColumnIndexOrThrow(MediaConstants.SONG_ARTIST))

            val data: String =
                getString(getColumnIndexOrThrow(MediaConstants.SONG_URI_PATH))

            val duration: Long =
                getLong(getColumnIndexOrThrow(MediaConstants.SONG_DURATION))

            val albumId: Long =
                getLong(getColumnIndexOrThrow(MediaConstants.SONG_ALBUM_ID))

            val albumArt = getAlbumArt(albumId)

            val song = Song(
                id = id,
                title = title,
                artist = artist,
                path = data,
                duration = duration,
                albumArt = albumArt
            )

            return song
        }
    }

    private fun getAlbumArt(albumId: Long): String {

        val albumUri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId
        )

        return albumUri.toString()
    }

    fun convertLongToDuration(duration: Long): String {
        val totalSeconds = duration / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
        }
    }

}