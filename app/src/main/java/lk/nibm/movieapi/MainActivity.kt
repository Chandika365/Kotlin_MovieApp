package lk.nibm.movieapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.JsonArray
import com.squareup.picasso.Picasso
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    lateinit var movieRec :RecyclerView
    var movieData = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieRec = findViewById(R.id.movie_re)
        movieRec.adapter = MovieAdapter()
        movieRec.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        fetchMovieData()

    }


    fun fetchMovieData(){
        val url ="https://api.themoviedb.org/4/list/3?page=1&api_key=5de87433d2c29c4931893dcb925547f6"
        val request = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { res ->
                movieData = res.getJSONArray("results")
                movieRec.adapter?.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            })
        Volley.newRequestQueue(this).add(request)
    }




    private inner class MovieAdapter : RecyclerView.Adapter<MovieDataHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie, parent,false)
            return MovieDataHolder(view)
        }
        override fun getItemCount(): Int {
            return movieData.length()
        }
        override fun onBindViewHolder(holder: MovieDataHolder, position: Int) {
            holder.movieTitle.text = movieData.getJSONObject(position).getString("title")
            holder.movieReleaseDate.text = movieData.getJSONObject(position).getString("release_date")
            val imageURL = "https://image.tmdb.org/t/p/w500" +
                    movieData.getJSONObject(position).getString("poster_path")
            Picasso.get().load(imageURL).into(holder.imgPoster)
        }
    }
    private inner class MovieDataHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var movieTitle : TextView = itemView.findViewById(R.id.txt_movieName)
        var movieReleaseDate : TextView = itemView.findViewById(R.id.txt_releaseDate)
        var imgPoster : ImageView = itemView.findViewById(R.id.imgPoster)
    }

}