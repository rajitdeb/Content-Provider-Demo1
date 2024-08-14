package com.rajit.contentprovidersample_musicplayerapp

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long,
    val albumArt: String
)
