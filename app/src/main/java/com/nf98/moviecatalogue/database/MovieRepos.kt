package com.nf98.moviecatalogue.database

import androidx.lifecycle.LiveData
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow

class MovieRepos(private val movieDao: MovieDao) {

    //movie
    suspend fun insert(movie: Movie){ movieDao.insertMovie(movie) }

    suspend fun delete(movie: Movie){ movieDao.deleteMovie(movie) }

    fun getAllMovie(): LiveData<List<Movie>> = movieDao.getAllMovie()

    //tvShow
    suspend fun insert(tvShow: TVShow){ movieDao.insertTVShow(tvShow) }

    suspend fun delete(tvShow: TVShow){ movieDao.deleteTVShow(tvShow) }

    fun getAllTVShow(): LiveData<List<TVShow>> = movieDao.getAllTVShow()
}