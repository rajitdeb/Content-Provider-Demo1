package com.rajit.contentprovidersample_musicplayerapp

import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi

object MediaConstants {

    const val SONG_ID = MediaStore.Audio.Media._ID
    const val SONG_TITLE = MediaStore.Audio.Media.TITLE
    const val SONG_ARTIST = MediaStore.Audio.Media.ARTIST
    const val SONG_URI_PATH = MediaStore.Audio.Media.DATA
    const val SONG_DURATION = MediaStore.Audio.Media.DURATION
    const val SONG_ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID

    const val STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    const val AUDIO_PERMISSION = android.Manifest.permission.READ_MEDIA_AUDIO

    const val PERMISSION_REQUEST_CODE = 1001

}