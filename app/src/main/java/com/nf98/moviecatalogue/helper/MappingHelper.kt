package com.nf98.moviecatalogue.helper

import android.database.Cursor
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.db.DatabaseContract

object MappingHelper {

    fun mapMovieCursorToArrayList(movieCursor: Cursor?): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()
        movieCursor?.apply {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE))
                val score = getFloat(getColumnIndexOrThrow(DatabaseContract.MovieColumns.SCORE))
                val overview = getString(getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW))
                val movieId = getInt(getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID))
                val poster = getString(getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH))
                movieList.add(Movie(id = movieId, title = title, releaseDate = date, score = score, overview = overview, posterPath = poster))
            }
        }
        return movieList
    }

    fun mapTVShowCursorToArrayList(tvShowCursor: Cursor?): ArrayList<TVShow> {
        val tvShowList = ArrayList<TVShow>()
        tvShowCursor?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.NAME))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.FIRST_AIR_DATE))
                val score = getFloat(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.SCORE))
                val overview = getString(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.OVERVIEW))
                val tvShowId = getInt(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.ID))
                val poster = getString(getColumnIndexOrThrow(DatabaseContract.TVShowColumns.POSTER_PATH))
                tvShowList.add(TVShow(id = tvShowId, name = name, firstAirDate = date, score = score, overview = overview, posterPath = poster))
            }
        }
        return tvShowList
    }

}