package com.example.hivebum

/*** Reference technique
 * @author Gabin
 * @since 25/04/2024
 * @see <a href="TODO">Spec</a>
 * @see <a href="TODO">CT</a>
 */

import android.content.Context
import com.example.hivebum.spotify.model.SpotifyModel
import com.example.hivebum.spotify.presenter.SpotifyPresenter
import com.example.hivebum.spotify.view.SpotifyView
import com.yuyakaido.android.cardstackview.Direction
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class SpotifyPresenterTest {

    private val mockModel = Mockito.mock(SpotifyModel::class.java)
    private val mockView = Mockito.mock(SpotifyView::class.java)
    private val spotifyPresenter = SpotifyPresenter(mockView, mock(Context::class.java)).apply {
        this.model = mockModel
    }

    @Test
    fun getAnswer_returnsTrue_whenDirectionIsRight() {
        Mockito.`when`(mockModel.getAnswer(Direction.Right)).thenReturn(true)
        val result = spotifyPresenter.getAnswer(Direction.Right)
        Assert.assertTrue(result)
    }

    @Test
    fun getAnswer_returnsFalse_whenDirectionIsLeft() {
        Mockito.`when`(mockModel.getAnswer(Direction.Left)).thenReturn(false)
        val result = spotifyPresenter.getAnswer(Direction.Left)
        Assert.assertFalse(result)
    }

    @Test
    fun getAnswer_returnsFalse_whenDirectionIsTop() {
        Mockito.`when`(mockModel.getAnswer(Direction.Top)).thenReturn(false)
        val result = spotifyPresenter.getAnswer(Direction.Top)
        Assert.assertFalse(result)
    }

    @Test
    fun getAnswer_returnsFalse_whenDirectionIsBottom() {
        Mockito.`when`(mockModel.getAnswer(Direction.Bottom)).thenReturn(false)
        val result = spotifyPresenter.getAnswer(Direction.Bottom)
        Assert.assertFalse(result)
    }
}