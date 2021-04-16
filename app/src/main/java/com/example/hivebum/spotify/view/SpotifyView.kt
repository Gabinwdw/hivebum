package com.example.hivebum.spotify.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.hivebum.R
import com.example.hivebum.spotify.presenter.SpotifyPresenter
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import com.yuyakaido.android.cardstackview.*
import soup.neumorphism.NeumorphImageView

class SpotifyView : AppCompatActivity(), CardStackListener {
    var getAccessToken: Boolean = false
    lateinit var accessToken: String
    private lateinit var presenter: SpotifyPresenter

    private lateinit var yesButton: NeumorphImageView
    private lateinit var noButton: NeumorphImageView
    private lateinit var nameGenreText: TextView
    private lateinit var revealLayout: View
    private lateinit var revealLayoutText: TextView

    private lateinit var cardView: CardStackView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPresenter(SpotifyPresenter(this, this))

        presenter.onViewCreated()
        presenter.connectionApiSpotfiy()

        init()
        initializeCardView()
    }

    fun init() {
        yesButton = this.findViewById(R.id.yes_button)
        noButton = this.findViewById(R.id.no_button)
        nameGenreText = this.findViewById(R.id.genreNameText)
        cardView = findViewById(R.id.card_stack_view)
        revealLayout = findViewById(R.id.revealAnswer)
        revealLayoutText = findViewById(R.id.revealAnswerText)

        nameGenreText.visibility = View.INVISIBLE

        yesButton.setOnClickListener {
            presenter.getAnswer(Direction.Right)
            cardView.swipe()
        }
        noButton.setOnClickListener {
            presenter.getAnswer(Direction.Left)
            cardView.swipe()
        }
    }

    private fun initializeCardView() {
        cardView.layoutManager = presenter.getCardViewManager(this)

        cardView.adapter = presenter.getCardViewAdapter()
        cardView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    fun getNewAlbum() {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
        val randomString = ("a").map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }.map(charPool::get).joinToString("")
        if (getAccessToken) {
            presenter.getAlbumSpotifySearch(accessToken, randomString)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == LoginActivity.REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    accessToken = response.accessToken
                    getAccessToken = true
                    presenter.getGenreSeedSpotify(accessToken)
                    getNewAlbum()
                    getNewAlbum()
                }
                AuthenticationResponse.Type.ERROR -> {
                    getAccessToken = false
                } else -> { }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun revealLayoutFun(answer : Boolean) {
        val x: Int = revealLayout.right / 2
        val y: Int = revealLayout.bottom / 2
        val startRadius = 0
        val endRadius = kotlin.math.hypot(
            revealLayout.width.toDouble(),
            revealLayout.height.toDouble()
        ).toInt()
        val anim = ViewAnimationUtils.createCircularReveal(revealLayout, x, y, startRadius.toFloat(), endRadius.toFloat())
        revealLayout.visibility = View.VISIBLE
        if (answer) {
            revealLayoutText.text = "You right !"
            revealLayout.setBackgroundResource(R.color.green_answer)
        } else {
            revealLayoutText.text = "You wrong !"
            revealLayout.setBackgroundResource(R.color.red_answer)
        }

        anim.duration = 500
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                unrevealLayoutFun()
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        anim.start()
    }

    fun unrevealLayoutFun() {
        val x: Int = revealLayout.right / 2
        val y: Int = revealLayout.bottom / 2

        val startRadius: Int = kotlin.math.max(revealLayout.width, revealLayout.height)
        val endRadius = 0
        val anim = ViewAnimationUtils.createCircularReveal(
            revealLayout,
            x,
            y,
            startRadius.toFloat(),
            endRadius.toFloat()
        )
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                revealLayout.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        anim.duration = 250
        anim.start()
    }

    fun displayAlbumPicture(artistId: String, albumUrl: String) {
        presenter.getArtistSpotifySearch(accessToken, artistId, albumUrl)
    }

    fun setGenreNameText(genreName: String) {
        nameGenreText.visibility = View.VISIBLE
        nameGenreText.text = genreName
    }

    private fun setPresenter(presenter: SpotifyPresenter) {
        this.presenter = presenter
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction) {
        val answer = presenter.getAnswer(direction)
        revealLayoutFun(answer)
        getNewAlbum()
    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {}
    override fun onCardAppeared(view: View, position: Int) {}
    override fun onCardDisappeared(view: View, position: Int) {}
}