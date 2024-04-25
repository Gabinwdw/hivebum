package com.example.hivebum.spotify.presenter

import android.content.Context
import com.example.hivebum.spotify.ApiResult.ApiResultAlbumSpotify
import com.example.hivebum.spotify.CardStackAdapter
import com.example.hivebum.spotify.model.SpotifyModel
import com.example.hivebum.spotify.view.SpotifyView
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class SpotifyPresenter(view: SpotifyView, context: Context) {

    private var view: SpotifyView? = view
    var model: SpotifyModel = SpotifyModel(context)

    fun onViewCreated() {
    }

    fun connectionApiSpotfiy() {
        model.connectionApiSpotfiy()
    }

    fun getArtistSpotifySearch(accessToken: String?, artistId: String, albumUrl: String) {
        model.getArtistSpotify(this, accessToken, artistId, albumUrl)
    }

    fun getAlbumSpotifySearch(accessToken : String?, album : String) {
        model.getAlbumSpotifySearch(this, accessToken, album)
    }

    fun getGenreSeedSpotify(accessToken: String?) {
        model.getGenreSeedSpotify(this, accessToken)
    }

    fun getCardViewAdapter(): CardStackAdapter {
        return model.setCardViewAdapter()
    }

    fun getCardViewManager(cardStackListener: CardStackListener) : CardStackLayoutManager {
        return model.setCardViewManager(cardStackListener)
    }

    fun getAnswer(direction: Direction) : Boolean {
        return model.getAnswer(direction)
    }

    fun onFinished(album: ApiResultAlbumSpotify) {
        view?.displayAlbumPicture(album.albums?.items?.get(0)?.artists?.get(0)?.id!!, album.albums?.items?.get(0)?.images?.get(0)?.url!!)
    }

    fun getNewAlbums() {
        view?.getNewAlbum()
    }

    fun setGenreText(genreText : String) {
        view?.setGenreNameText(genreText)
    }

    fun onFailure() {

    }
}