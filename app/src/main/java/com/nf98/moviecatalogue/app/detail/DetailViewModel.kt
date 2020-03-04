package com.nf98.moviecatalogue.app.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nf98.moviecatalogue.api.ApiMain
import com.nf98.moviecatalogue.api.model.Credit
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.api.response.CreditsResponse
import com.nf98.moviecatalogue.database.MovieRepos
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DetailViewModel(private val movieRepos: MovieRepos? = null) : ViewModel() {

    companion object{
        const val MOVIE = 0
        const val TV = 1
    }

    val movie = MutableLiveData<Movie>()
    val tvShow = MutableLiveData<TVShow>()

    val casts = MutableLiveData<ArrayList<Credit>>()
    val crews = MutableLiveData<ArrayList<Credit>>()

    internal fun getMovieList(): LiveData<List<Movie>>? = movieRepos?.getAllMovie()

    fun insertMovie(movie: Movie){
        viewModelScope.launch {
            movieRepos?.insert(movie)
        }
    }

    fun deleteMovie(movie: Movie){
        viewModelScope.launch {
            movieRepos?.delete(movie)
        }
    }

    internal fun getMovie(id: Int): LiveData<Movie> {
        val result = ApiMain().services.getMovie(id)

        result.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if(response.isSuccessful)
                    response.body().let { movie.postValue(it) }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("MovieDB: ", t.message)
            }
        })
        return movie
    }

    internal fun getTVShow(id: Int): LiveData<TVShow> {
        val result = ApiMain().services.getTVShow(id)

        result.enqueue(object : Callback<TVShow> {
            override fun onResponse(call: Call<TVShow>, response: Response<TVShow>) {
                if(response.isSuccessful)
                    response.body().let { tvShow.postValue(it) }
            }
            override fun onFailure(call: Call<TVShow>, t: Throwable) {
                Log.d("MovieDB: ", t.message)
            }
        })
        return tvShow
    }

    internal fun getCasts(type: Int, id: Int): LiveData<ArrayList<Credit>> {
        val result = when (type) {
            MOVIE -> ApiMain().services.getMovieCredits(id)
            TV -> ApiMain().services.getTVCredits(id)
            else -> throw IllegalArgumentException("Invalid type")
        }

        result.enqueue(object : Callback<CreditsResponse> {
            override fun onResponse(call: Call<CreditsResponse>, response: Response<CreditsResponse>) {
                if(response.code() == 200)
                    response.body()?.cast.let { casts.postValue(it) }
            }
            override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {

            }
        })
        return casts
    }

    internal fun getCrews(type: Int, id: Int): LiveData<ArrayList<Credit>> {
        val result = when (type) {
            MOVIE -> ApiMain().services.getMovieCredits(id)
            TV -> ApiMain().services.getTVCredits(id)
            else -> throw IllegalArgumentException("Invalid type")
        }

        result.enqueue(object : Callback<CreditsResponse> {
            override fun onResponse(call: Call<CreditsResponse>, response: Response<CreditsResponse>) {
                if(response.code() == 200)
                    response.body()?.crew.let { crews.postValue(it) }
            }
            override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {

            }
        })
        return crews
    }
}