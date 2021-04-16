package com.example.hivebum.spotify.ApiResult

import com.google.gson.annotations.SerializedName

data class ApiResultGenreSeedSpotify(var genreSeendId: String?) {
    @SerializedName("genres")
    var genres: List<String>? = null
}
