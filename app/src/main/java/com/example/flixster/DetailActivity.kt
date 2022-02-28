package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import org.w3c.dom.Text

private const val VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=$TMDB_API_KEY"

class DetailActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val player = findViewById<YouTubePlayerView>(R.id.player)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvOverView = findViewById<TextView>(R.id.tvOverview)
        val rating = findViewById<RatingBar>(R.id.ratingBar)
        val date = findViewById<TextView>(R.id.tvDate)


        val movie = intent.getParcelableExtra<Movie>("movie")!!

        tvTitle.text = movie.title
        tvOverView.text = movie.overview
        rating.rating = movie.rating.toFloat()
        date.text = movie.releaseDate

        val client = AsyncHttpClient()
        client.get(VIDEO_URL.format(movie.movieId), object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("DetailActivity","Request for video urls failed $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val results = json.jsonObject.getJSONArray("results")

                //got a youtube trailer?
                var ytKey: String? = null

                for (i in 0 until results.length()){
                    val video = results.getJSONObject(i)
                    if (video.getString("site") == "YouTube" && video.getString("type") == "Trailer") {
                        ytKey = video.getString("key")
                        break
                    }
                }

                if (ytKey == null){
                    Log.w("DetailActivity", "No associated videos with movie ${movie.movieId}")
                    return
                }

                initializeWithYouTubeKey(player,ytKey)
            }

        })



    }

    private fun initializeWithYouTubeKey(player: YouTubePlayerView, ytKey: String) {
        player.initialize(Companion.YOUTUBE_KEY,object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {

                p1?.cueVideo(ytKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.e("DetailActivity","Could not initialize YouTubePlayer")
            }

        })
    }

    companion object {
        private const val YOUTUBE_KEY = "AIzaSyCn0fWwjoqhbpqxspRHPucHY0bxJ1Z95m0" //go ahead and steal this, I don't care
    }

}