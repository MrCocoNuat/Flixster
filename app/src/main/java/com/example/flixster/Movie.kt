package com.example.flixster

import org.json.JSONArray

data class Movie (
    val movieId: Int,
    val title : String,
    val overview: String,
    private val posterPath : String,
    private val backdropPath : String,
){
    val posterUrl =  "https://image.tmdb.org/t/p/w342/$posterPath"
    val backdropUrl = "https://image.tmdb.org/t/p/w342/$backdropPath"
    companion object {
        fun fromJsonArray(movieJSONArray: JSONArray) : List<Movie> {
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJSONArray.length()){
                val movieJSON = movieJSONArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJSON.getInt("id"),
                        movieJSON.getString("title"),
                        movieJSON.getString("overview"),
                        movieJSON.getString("poster_path"), //naughty naughty
                        movieJSON.getString("backdrop_path")
                    )
                )
            }

            return movies
        }
    }
}