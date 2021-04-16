package com.example.hivebum.spotify.ApiResult

import com.google.gson.annotations.SerializedName

data class ApiResultAlbumSpotify(var albumId: String?) {
    @SerializedName("albums")
    var albums: Albums? = null
}

class ExternalUrls {
    @SerializedName("spotify")
    var spotify: String? = null
}

class Albums {
    @SerializedName("href")
    var href: String? = null
    @SerializedName("items")
    var items: List<Item>? = null
    @SerializedName("limit")
    var limit = 0
    @SerializedName("next")
    var next: String? = null
    @SerializedName("offset")
    var offset = 0
    @SerializedName("previous")
    var previous: Any? = null
    @SerializedName("total")
    var total = 0
}

class Image {
    @SerializedName("height")
    var height = 0
    @SerializedName("url")
    var url: String? = null
    @SerializedName("width")
    var width = 0
}

class Item {
    @SerializedName("album_type")
    var albumType: String? = null
    @SerializedName("artists")
    var artists: List<Artist>? = null
    @SerializedName("available_markets")
    var availableMarkets: List<String>? = null
    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null
    @SerializedName("href")
    var href: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("images")
    var images: List<Image>? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("release_date")
    var releaseDate: String? = null
    @SerializedName("release_date_precision")
    var releaseDatePrecision: String? = null
    @SerializedName("total_tracks")
    var totalTracks = 0
    @SerializedName("type")
    var type: String? = null
    @SerializedName("uri")
    var uri: String? = null
}

class Artist {
    @SerializedName("external_urls")
    var externalUrls: ExternalUrls? = null
    @SerializedName("href")
    var href: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("uri")
    var uri: String? = null
}
