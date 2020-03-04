package com.nf98.moviecatalogue.database

import androidx.lifecycle.LiveData
import com.nf98.moviecatalogue.api.model.Movie

class MovieRepos(private val movieDao: MovieDao) {

    suspend fun insert(movie: Movie){
        movieDao.insertMovie(movie)
    }

    suspend fun delete(movie: Movie){
        movieDao.deleteMovie(movie)
    }

    fun getAllMovie(): LiveData<List<Movie>> = movieDao.getAllMovie()

    fun getMovie(id: Int): LiveData<Movie> = movieDao.getMovie(id)
}