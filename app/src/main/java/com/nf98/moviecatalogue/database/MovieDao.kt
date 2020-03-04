package com.nf98.moviecatalogue.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nf98.moviecatalogue.api.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("select * from movie")
    fun getAllMovie(): LiveData<List<Movie>>

    @Query("select * from movie where id = :id limit 1")
    fun getMovie(id: Int): LiveData<Movie>
}