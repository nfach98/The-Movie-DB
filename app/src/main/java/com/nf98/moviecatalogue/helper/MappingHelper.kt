package com.nf98.moviecatalogue.helper

import android.database.Cursor
import com.nf98.moviecatalogue.api.model.Genre
import com.nf98.moviecatalogue.api.model.Movie

object MappingHelper {

    private val converter = DataConverter()

    fun mapCursorToMovieList(cursor: Cursor?): ArrayList<Movie> {
        val list = ArrayList<Movie>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Movie.ID))
                val popularity = getFloat(getColumnIndexOrThrow(Movie.POPULARITY))
                val voteCount = getInt(getColumnIndexOrThrow(Movie.VOTE_COUNT))
                val duration = getInt(getColumnIndexOrThrow(Movie.DURATION))
                val originalLanguage = getString(getColumnIndexOrThrow(Movie.ORI_LANGUAGE))
                val originalTitle = getString(getColumnIndexOrThrow(Movie.ORI_TITLE))
                val genres = getString(getColumnIndexOrThrow(Movie.GENRES))
                val title = getString(getColumnIndexOrThrow(Movie.TITLE))
                val status = getString(getColumnIndexOrThrow(Movie.STATUS))
                val budget = getInt(getColumnIndexOrThrow(Movie.BUDGET))
                val revenue = getInt(getColumnIndexOrThrow(Movie.REVENUE))
                val score = getFloat(getColumnIndexOrThrow(Movie.SCORE))
                val overview = getString(getColumnIndexOrThrow(Movie.OVERVIEW))
                val date = getString(getColumnIndexOrThrow(Movie.RELEASE_DATE))
                list.add(Movie(id = id, popularity = popularity, voteCount = voteCount, duration = duration, originalLanguage = originalLanguage,
                    originalTitle = originalTitle, genres = converter.getGenres(genres), title = title, status = status, budget = budget, revenue = revenue,
                    score = score, overview = overview, releaseDate = date))
            }
        }
        return list
    }

    fun mapCursorToMovie(cursor: Cursor?): Movie {
        var movie = Movie()

        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(Movie.ID))
            val popularity = getFloat(getColumnIndexOrThrow(Movie.POPULARITY))
            val voteCount = getInt(getColumnIndexOrThrow(Movie.VOTE_COUNT))
            val duration = getInt(getColumnIndexOrThrow(Movie.DURATION))
            val originalLanguage = getString(getColumnIndexOrThrow(Movie.ORI_LANGUAGE))
            val originalTitle = getString(getColumnIndexOrThrow(Movie.ORI_TITLE))
            val genres = getString(getColumnIndexOrThrow(Movie.GENRES))
            val title = getString(getColumnIndexOrThrow(Movie.TITLE))
            val status = getString(getColumnIndexOrThrow(Movie.STATUS))
            val budget = getInt(getColumnIndexOrThrow(Movie.BUDGET))
            val revenue = getInt(getColumnIndexOrThrow(Movie.REVENUE))
            val score = getFloat(getColumnIndexOrThrow(Movie.SCORE))
            val overview = getString(getColumnIndexOrThrow(Movie.OVERVIEW))
            val date = getString(getColumnIndexOrThrow(Movie.RELEASE_DATE))
            movie = Movie(id = id, popularity = popularity, voteCount = voteCount, duration = duration, originalLanguage = originalLanguage,
                originalTitle = originalTitle, genres = converter.getGenres(genres), title = title, status = status, budget = budget, revenue = revenue,
                score = score, overview = overview, releaseDate = date)
        }
        return movie
    }
}