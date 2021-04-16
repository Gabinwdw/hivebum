package com.example.hivebum.spotify.model

import android.app.Activity
import android.content.Context
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.example.hivebum.spotify.AlbumData
import com.example.hivebum.spotify.ApiClientSpotify
import com.example.hivebum.spotify.ApiInterfaceSpotify
import com.example.hivebum.spotify.ApiResult.ApiResultAlbumSpotify
import com.example.hivebum.spotify.ApiResult.ApiResultArtistSpotify
import com.example.hivebum.spotify.ApiResult.ApiResultGenreSeedSpotify
import com.example.hivebum.spotify.CardStackAdapter
import com.example.hivebum.spotify.presenter.SpotifyPresenter
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import com.yuyakaido.android.cardstackview.*
import retrofit2.Call
import retrofit2.Callback
import java.util.ArrayList
import kotlin.random.Random

class SpotifyModel(private var context: Context) {

    private val clientId = "d87deeacab774531b8d9a138b4e4593e"
    private val redirectUri = "http://com.example.hivebum/callback"
    private var api : ApiInterfaceSpotify? = ApiClientSpotify.retrofit.create(ApiInterfaceSpotify::class.java)

    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    private var listAlbums: ArrayList<AlbumData> = ArrayList<AlbumData>()
    private lateinit var seedGenres: List<String>

    fun connectionApiSpotfiy() {
        val builder = AuthenticationRequest.Builder(
            clientId,
            AuthenticationResponse.Type.TOKEN,
            redirectUri
        )
        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        AuthenticationClient.openLoginActivity(context as Activity?, LoginActivity.REQUEST_CODE, request)
    }

    fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getAlbumSpotifySearch(onFinishedListener: SpotifyPresenter, accessToken : String?, album : String) {
        val call : Call<ApiResultAlbumSpotify>? = api?.searchAlbum("$album%", "Bearer $accessToken")

        call?.enqueue(object : Callback<ApiResultAlbumSpotify> {

            override fun onResponse(call: Call<ApiResultAlbumSpotify>, response: retrofit2.Response<ApiResultAlbumSpotify>) {
                val responseRes : ApiResultAlbumSpotify? = response.body()

                if (responseRes == null) {
                    displayToast("Code : " + response.code())
                    onFinishedListener.onFailure()
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ApiResultAlbumSpotify>, t: Throwable) {
                displayToast(t.toString())
            }
        })
    }

    fun getArtistSpotify(onFinishedListener: SpotifyPresenter, accessToken : String?, artistId : String, albumUrl: String) {
        val call : Call<ApiResultArtistSpotify>? = api?.getArtistById(artistId, "Bearer $accessToken")

        call?.enqueue(object : Callback<ApiResultArtistSpotify> {

            override fun onResponse(call: Call<ApiResultArtistSpotify>, response: retrofit2.Response<ApiResultArtistSpotify>) {
                val responseRes : ApiResultArtistSpotify? = response.body()

                if (responseRes == null) {
                    displayToast("Code : " + response.code())
                    onFinishedListener.onFailure()
                } else {
                    if(!addNewAlbum(responseRes, albumUrl))
                        onFinishedListener.getNewAblums()
                    else
                        onFinishedListener.setGenreText(listAlbums[0].genre)
                }
            }

            override fun onFailure(call: Call<ApiResultArtistSpotify>, t: Throwable) {
                displayToast(t.toString())
            }
        })
    }

    fun getGenreSeedSpotify(onFinishedListener: SpotifyPresenter, accessToken : String?) {
        val call : Call<ApiResultGenreSeedSpotify>? = api?.getGenreSeeds("Bearer $accessToken")

        call?.enqueue(object : Callback<ApiResultGenreSeedSpotify> {

            override fun onResponse(call: Call<ApiResultGenreSeedSpotify>, response: retrofit2.Response<ApiResultGenreSeedSpotify>) {
                val responseRes : ApiResultGenreSeedSpotify? = response.body()

                if (responseRes == null) {
                    displayToast("Code : " + response.code())
                    onFinishedListener.onFailure()
                } else {
                    seedGenres = responseRes.genres!!
                }
            }

            override fun onFailure(call: Call<ApiResultGenreSeedSpotify>, t: Throwable) {
                displayToast(t.toString())
            }
        })
    }

    fun setCardViewManager(cardStackListener: CardStackListener) : CardStackLayoutManager {
        manager = CardStackLayoutManager(context, cardStackListener)

        manager.setStackFrom(StackFrom.Top)
        manager.setVisibleCount(1)
        manager.setTranslationInterval(12f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        return manager
    }

    fun setCardViewAdapter() : CardStackAdapter {
        adapter = CardStackAdapter(listAlbums)
        return adapter
    }

    fun addNewAlbum(artist: ApiResultArtistSpotify, albumUrl: String) : Boolean {
        if (artist.genres?.isEmpty() == true) {
            return false
        }
        if (Random.nextBoolean()) {
            val i = Random.nextInt(0, artist.genres!!.size)
            listAlbums.add(AlbumData(genre = artist.genres!![i], url = albumUrl, genreArtist = artist.genres!!))
        } else {
            val i = Random.nextInt(0, seedGenres.size)
            listAlbums.add(AlbumData(genre = seedGenres[i], url = albumUrl, genreArtist = artist.genres!!))
        }
        if (listAlbums.size > 2)
            listAlbums.removeAt(0)
        adapter.notifyDataSetChanged()
        return true
    }

    fun getAnswer(direction: Direction) : Boolean {

        if (listAlbums.isEmpty())
            return true

        if (direction == Direction.Right) {
            val genreFound = listAlbums[0].genreArtist.find { it == listAlbums[0].genre }
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            return genreFound != null
        } else {
            val genreFound = listAlbums[0].genreArtist.find { it == listAlbums[0].genre }
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            return genreFound == null
        }
    }
}