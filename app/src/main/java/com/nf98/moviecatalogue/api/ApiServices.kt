package com.nf98.moviecatalogue.api

import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.api.response.CreditsResponse
import com.nf98.moviecatalogue.api.response.MovieResponse
import com.nf98.moviecatalogue.api.response.TVShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("movie/now_playing?api_key=${ApiMain.API_KEY}&page=1")
    fun getNowPlayingMovies(@Query("region") region: String): Call<MovieResponse>

    @GET("movie/popular?api_key=${ApiMain.API_KEY}&page=1")
    fun getPopularMovies(@Query("region") region: String): Call<MovieResponse>

    @GET("movie/top_rated?api_key=${ApiMain.API_KEY}&page=1")
    fun getTopRatedMovies(@Query("region") region: String): Call<MovieResponse>

    @GET("movie/upcoming?api_key=${ApiMain.API_KEY}&page=1")
    fun getUpcomingMovies(@Query("region") region: String): Call<MovieResponse>


    @GET("tv/airing_today?api_key=${ApiMain.API_KEY}&page=1")
    fun getAiringTodayTV(): Call<TVShowResponse>

    @GET("tv/popular?api_key=${ApiMain.API_KEY}&page=1")
    fun getPopularTV(): Call<TVShowResponse>

    @GET("tv/on_the_air?api_key=${ApiMain.API_KEY}&page=1")
    fun getOnTheAirTV(): Call<TVShowResponse>

    @GET("tv/top_rated?api_key=${ApiMain.API_KEY}&page=1")
    fun getTopRatedTV(): Call<TVShowResponse>


    @GET("discover/movie?api_key=${ApiMain.API_KEY}&page=1")
    fun getDiscoverMovie(@Query("region") region: String,
                         @Query("primary_release_year") year: Int,
                         @Query("sort_by") sort_by: String): Call<MovieResponse>

    @GET("discover/tv?api_key=${ApiMain.API_KEY}&page=1")
    fun getDiscoverTV(@Query("first_air_date_year") year: Int,
                      @Query("sort_by") sort_by: String): Call<TVShowResponse>


    @GET("movie/{id}?api_key=${ApiMain.API_KEY}")
    fun getMovie(@Path("id") id: Int): Call<Movie>

    @GET("movie/{id}/credits?api_key=${ApiMain.API_KEY}")
    fun getMovieCredits(@Path("id") id: Int): Call<CreditsResponse>

    @GET("tv/{id}?api_key=${ApiMain.API_KEY}")
    fun getTVShow(@Path("id") id: Int): Call<TVShow>

    @GET("tv/{id}/credits?api_key=${ApiMain.API_KEY}")
    fun getTVCredits(@Path("id") id: Int): Call<CreditsResponse>
}