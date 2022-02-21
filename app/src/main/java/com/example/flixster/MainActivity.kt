package com.example.flixster

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rv)

        val movieAdapter = MovieItemAdapter(this, movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler(){
            override fun onFailure(statusCode: Int, p1: Headers?, p2: String?, p3: Throwable?) {
                Log.e(TAG,"onFailure: Error Code $statusCode")
            }

            override fun onSuccess(statusCode: Int, p1: Headers?, p2: JSON) {
                Log.i(TAG,"onSuccess: received $p2")


                try {
                    val movieJSONArray = p2.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJSONArray))
                    movies.sortBy{it.title}
                    Log.i(TAG, "Parsed into list of Movie: $movies")

                    movieAdapter.notifyDataSetChanged() //called only once so it is ok
                } catch (e: JSONException){
                    Log.e(TAG, "JSONException $e !")
                }





            }

        })


    }
}