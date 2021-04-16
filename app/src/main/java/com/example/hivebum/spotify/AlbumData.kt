package com.example.hivebum.spotify

data class AlbumData(
    val id: Long = counter++,
    val genre: String,
    val genreArtist: List<String>,
    val url: String
) {
    companion object {
        private var counter = 0L
    }
}