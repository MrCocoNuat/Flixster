package com.example.flixster

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieItemAdapter(private val context: Context,
                       private val movies: List<Movie>,
                       val clickListener : OnClickListener
) :
    RecyclerView.Adapter<MovieItemAdapter.ViewHolder>() {


    //implemented by obj in MainActivity
    interface OnClickListener{
        fun onMovieClicked(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val posterView = itemView.findViewById<ImageView>(R.id.poster)
        private val titleView = itemView.findViewById<TextView>(R.id.title)
        private val overviewView = itemView.findViewById<TextView>(R.id.overview)

        init{
            itemView.setOnClickListener{
                clickListener.onMovieClicked(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            titleView.text = movie.title
            overviewView.text = movie.overview

            //orientation dependent
            //if anything but landscape just use the poster image
            val orientation = context.resources.configuration.orientation
            val imageUrl = if (orientation == Configuration.ORIENTATION_LANDSCAPE) movie.backdropUrl
             else movie.posterUrl

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    // DO NOT alter the viewport size to match the placeholder, keep it the same
                .dontTransform()
                .error(R.drawable.ic_baseline_cloud_off_24)
                .dontTransform()
                .into(posterView)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

