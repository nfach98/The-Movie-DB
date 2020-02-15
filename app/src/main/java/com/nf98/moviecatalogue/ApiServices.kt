package com.nf98.moviecatalogue

import com.nf98.moviecatalogue.response.MovieResponse
import com.nf98.moviecatalogue.response.TVShowResponse
import com.nf98.moviecatalogue.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Year

interface ApiServices {

    @GET("movie/now_playing?api_key=${MainViewModel.API_KEY}&language=en-US&page=1")
    fun getNowPlayingMovies(): Call<MovieResponse>

    @GET("movie/popular?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getPopularMovies(): Call<MovieResponse>

    @GET("movie/top_rated?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getTopRatedMovies(): Call<MovieResponse>

    @GET("movie/upcoming?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getUpcomingMovies(): Call<MovieResponse>


    @GET("tv/airing_today?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getAiringTodayTV(): Call<TVShowResponse>

    @GET("tv/popular?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getPopularTV(): Call<TVShowResponse>

    @GET("tv/on_the_air?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getOnTheAirTV(): Call<TVShowResponse>

    @GET("tv/top_rated?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getTopRatedTV(): Call<TVShowResponse>


    @GET("discover/movie?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getDiscoverMovie(@Query("primary_release_year") year: Int,
                         @Query("sort_by") sort_by: String): Call<MovieResponse>

    @GET("discover/tv?api_key=f1fb599be2a8084210ab493502e6c728&language=en-US&page=1")
    fun getDiscoverTV(@Query("first_air_date_year") year: Int,
                      @Query("sort_by") sort_by: String): Call<TVShowResponse>
}