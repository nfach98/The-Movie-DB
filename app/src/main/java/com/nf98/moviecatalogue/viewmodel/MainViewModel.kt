package com.nf98.moviecatalogue.viewmodel

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nf98.moviecatalogue.ApiMain
import com.nf98.moviecatalogue.response.MovieResponse
import com.nf98.moviecatalogue.model.Movie
import com.nf98.moviecatalogue.model.TVShow
import com.nf98.moviecatalogue.response.TVShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    companion object {
        const val API_KEY = "f1fb599be2a8084210ab493502e6c728"
        const val language = "en-US"
        const val page = 1

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
        var result: Call<MovieResponse>? = null
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

        when (index) {
            MOVIE_POPULAR -> result = ApiMain().services.getPopularMovies()
            MOVIE_TOP_RATED -> result = ApiMain().services.getTopRatedMovies()
            MOVIE_UPCOMING -> result = ApiMain().services.getUpcomingMovies()
            MOVIE_NOW_PLAYING -> result = ApiMain().services.getNowPlayingMovies()

            MOVIE_DISCOVER -> result = ApiMain().services.getDiscoverMovie(year, sort)
        }

        result?.enqueue(object : Callback<MovieResponse> {
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

    @JvmOverloads
    internal fun getTVList(index: Int,
                           year: Int = Calendar.getInstance().get(Calendar.YEAR),
                           sortBy: Int = 0): LiveData<ArrayList<TVShow>> {
        var result: Call<TVShowResponse>? = null
        val sort = when (sortBy) {
            0 -> "popularity.desc"
            1 -> "popularity.asc"
            2 -> "vote_average.desc"
            3 -> "vote_average.asc"
            4 -> "first_air_date.desc"
            5 -> "first_air_date.asc"
            else -> "popularity.desc"
        }

        when (index) {
            TV_POPULAR -> result = ApiMain().services.getPopularTV()
            TV_TOP_RATED -> result = ApiMain().services.getTopRatedTV()
            TV_ON_TV -> result = ApiMain().services.getOnTheAirTV()
            TV_AIRING_TODAY -> result = ApiMain().services.getAiringTodayTV()

            TV_DISCOVER -> result = ApiMain().services.getDiscoverTV(year, sort)
        }

        result?.enqueue(object : Callback<TVShowResponse> {
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

    internal fun getYearList(): ArrayList<String> {
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        val list = arrayListOf<String>()

        for(i in yearNow downTo 1900)
            list.add(i.toString())

        return list
    }

}