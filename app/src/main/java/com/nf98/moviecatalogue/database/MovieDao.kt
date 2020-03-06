package com.nf98.moviecatalogue.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow

@Dao
interface MovieDao {
    //movie
    @Insert
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("select * from movie order by rowid")
    fun getAllMovie(): LiveData<List<Movie>>

    @Query("select * from movie where id = :id limit 1")
    fun getMovie(id: Int): LiveData<Movie>

    //tvShow
    @Insert
    suspend fun insertTVShow(tvShow: TVShow)

    @Delete
    suspend fun deleteTVShow(tvShow: TVShow)

    @Query("select * from tv_show order by rowid")
    fun getAllTVShow(): LiveData<List<TVShow>>

    @Query("select * from tv_show where id = :id limit 1")
    fun getTVShow(id: Int): LiveData<TVShow>
}