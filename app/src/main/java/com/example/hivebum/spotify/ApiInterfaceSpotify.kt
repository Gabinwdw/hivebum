package com.example.hivebum.spotify

import com.example.hivebum.spotify.ApiResult.ApiResultAlbumSpotify
import com.example.hivebum.spotify.ApiResult.ApiResultArtistSpotify
import com.example.hivebum.spotify.ApiResult.ApiResultGenreSeedSpotify
import retrofit2.Call
import retrofit2.http.*
import kotlin.random.Random

interface ApiInterfaceSpotify {

    @GET("search")
    fun searchAlbum(@Query("q") artistName: String,
                       @Header("Authorization") token: String,
                       @Header("Accept") accept: String = "application/json",
                       @Header("Content-Type") content: String = "application/json",
                       @Query("type") type : String = "album",
                       @Query("limit") limit : String = "1",
                       @Query("offset") offset : String = Random.nextInt(1, 1000).toString()

    ) : Call<ApiResultAlbumSpotify>

    @GET("artists/{id}")
    fun getArtistById(@Path("id") artistId: String,
                      @Header("Authorization") token: String,
                      @Header("Accept") accept: String = "application/json",
                      @Header("Content-Type") content: String = "application/json"
    ) : Call<ApiResultArtistSpotify>

    @GET("recommendations/available-genre-seeds")
    fun getGenreSeeds(
            @Header("Authorization") token: String,
            @Header("Accept") accept: String = "application/json",
            @Header("Content-Type") content: String = "application/json"
    ) : Call<ApiResultGenreSeedSpotify>
}