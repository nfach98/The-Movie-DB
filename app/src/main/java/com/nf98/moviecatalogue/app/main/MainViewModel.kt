package com.nf98.moviecatalogue.app.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nf98.moviecatalogue.api.ApiMain
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.api.response.MovieResponse
import com.nf98.moviecatalogue.api.response.TVShowResponse
import com.nf98.moviecatalogue.database.MovieRepos
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val movieRepos: MovieRepos) : ViewModel() {

    companion object {
        @SuppressLint("ConstantLocale")
        val region: String = Locale.getDefault().country

        const val MOVIE_POPULAR = 0
        const val MOVIE_TOP_RATED = 1
        const val MOVIE_UPCOMING = 2
        const val MOVIE_NOW_PLAYING = 3

        const val TV_POPULAR = 4
        const val TV_TOP_RATED = 5
        const val TV_ON_TV = 6
        const val TV_AIRING_TODAY = 7

        const val MOVIE_DISCOVER = 8
        const val TV_DISCOVER = 9
    }

    val listMovie = MutableLiveData<ArrayList<Movie>>()
    val listTVShow = MutableLiveData<ArrayList<TVShow>>()

    @JvmOverloads
    internal fun getMovieList(index: Int,
                              year: Int = Calendar.getInstance().get(Calendar.YEAR),
                              sortBy: Int = 0): LiveData<ArrayList<Movie>> {
        val sort = when (sortBy) {
            0 -> "popularity.desc"
            1 -> "popularity.asc"
            2 -> "vote_average.desc"
            3 -> "vote_average.asc"
            4 -> "release_date.desc"
            5 -> "release_date.asc"
            6 -> "original_title.desc"
            7 -> "original_title.asc"
            else -> "popularity.desc"
        }

        val result = when (index) {
            MOVIE_POPULAR -> ApiMain().services.getPopularMovies(
                region
            )
            MOVIE_TOP_RATED -> ApiMain().services.getTopRatedMovies(
                region
            )
            MOVIE_UPCOMING -> ApiMain().services.getUpcomingMovies(
                region
            )
            MOVIE_NOW_PLAYING -> ApiMain().services.getNowPlayingMovies(
                region
            )
            MOVIE_DISCOVER -> ApiMain().services.getDiscoverMovie(
                region, year, sort)
            else -> throw IllegalArgumentException("Invalid type")
        }

        result.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if(response.code() == 200)
                    response.body()?.movies.let { listMovie.postValue(it) }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("MovieDB: ", t.message)
            }
        })
        return listMovie
    }

    fun getMovieList(): LiveData<List<Movie>> = movieRepos.getAllMovie()

    fun deleteMovie(movie: Movie){
        viewModelScope.launch {
            movieRepos.delete(movie)
        }
    }

    @JvmOverloads
    internal fun getTVList(index: Int,
                           year: Int = Calendar.getInstance().get(Calendar.YEAR),
                           sortBy: Int = 0): LiveData<ArrayList<TVShow>> {
        val sort = when (sortBy) {
            0 -> "popularity.desc"
            1 -> "popularity.asc"
            2 -> "vote_average.desc"
            3 -> "vote_average.asc"
            4 -> "first_air_date.desc"
            5 -> "first_air_date.asc"
            else -> "popularity.desc"
        }

        val result = when (index) {
            TV_POPULAR -> ApiMain().services.getPopularTV()
            TV_TOP_RATED -> ApiMain().services.getTopRatedTV()
            TV_ON_TV -> ApiMain().services.getOnTheAirTV()
            TV_AIRING_TODAY -> ApiMain().services.getAiringTodayTV()
            TV_DISCOVER -> ApiMain().services.getDiscoverTV(year, sort)
            else -> throw IllegalArgumentException("Invalid type")
        }

        result.enqueue(object : Callback<TVShowResponse> {
            override fun onResponse(call: Call<TVShowResponse>, response: Response<TVShowResponse>) {
                if(response.code() == 200)
                    response.body()?.tvShows.let { listTVShow.postValue(it) }
            }
            override fun onFailure(call: Call<TVShowResponse>, t: Throwable) {
                Log.d("MovieDB: ", t.message)
            }
        })
        return listTVShow
    }

    fun getTVList(): LiveData<List<TVShow>> = movieRepos.getAllTVShow()

    fun deleteTV(tvShow: TVShow){
        viewModelScope.launch {
            movieRepos.delete(tvShow)
        }
    }

    internal fun getYearList(): ArrayList<String> {
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        val list = arrayListOf<String>()

        for(i in yearNow downTo 1900)
            list.add(i.toString())

        return list
    }

}