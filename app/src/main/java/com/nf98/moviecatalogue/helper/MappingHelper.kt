package com.nf98.moviecatalogue.helper

import android.database.Cursor
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow

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


    fun mapCursorToTVShowList(cursor: Cursor?): ArrayList<TVShow> {
        val list = ArrayList<TVShow>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(TVShow.ID))
                val popularity = getFloat(getColumnIndexOrThrow(TVShow.POPULARITY))
                val voteCount = getInt(getColumnIndexOrThrow(TVShow.VOTE_COUNT))
                val duration = getString(getColumnIndexOrThrow(TVShow.RUNTIMES))
                val originalLanguage = getString(getColumnIndexOrThrow(TVShow.ORI_LANGUAGE))
                val originalName = getString(getColumnIndexOrThrow(TVShow.ORI_NAME))
                val genres = getString(getColumnIndexOrThrow(TVShow.GENRES))
                val name = getString(getColumnIndexOrThrow(TVShow.NAME))
                val status = getString(getColumnIndexOrThrow(TVShow.STATUS))
                val score = getFloat(getColumnIndexOrThrow(TVShow.SCORE))
                val overview = getString(getColumnIndexOrThrow(TVShow.OVERVIEW))
                val date = getString(getColumnIndexOrThrow(TVShow.FIRST_AIR_DATE))
                val numSeasons = getInt(getColumnIndexOrThrow(TVShow.NUM_SEASONS))
                list.add(
                    TVShow(id = id, popularity = popularity, voteCount = voteCount, duration = converter.getTVDuration(duration), originalLanguage = originalLanguage,
                    originalName = originalName, genres = converter.getGenres(genres), name = name, status = status, number_of_seasons = numSeasons,
                    score = score, overview = overview, firstAirDate = date)
                )
            }
        }
        return list
    }

    fun mapCursorToTVShow(cursor: Cursor?): TVShow {
        var tvShow = TVShow()

        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(TVShow.ID))
            val popularity = getFloat(getColumnIndexOrThrow(TVShow.POPULARITY))
            val voteCount = getInt(getColumnIndexOrThrow(TVShow.VOTE_COUNT))
            val duration = getString(getColumnIndexOrThrow(TVShow.RUNTIMES))
            val originalLanguage = getString(getColumnIndexOrThrow(TVShow.ORI_LANGUAGE))
            val originalName = getString(getColumnIndexOrThrow(TVShow.ORI_NAME))
            val genres = getString(getColumnIndexOrThrow(TVShow.GENRES))
            val name = getString(getColumnIndexOrThrow(TVShow.NAME))
            val status = getString(getColumnIndexOrThrow(TVShow.STATUS))
            val score = getFloat(getColumnIndexOrThrow(TVShow.SCORE))
            val overview = getString(getColumnIndexOrThrow(TVShow.OVERVIEW))
            val date = getString(getColumnIndexOrThrow(TVShow.FIRST_AIR_DATE))
            val numSeasons = getInt(getColumnIndexOrThrow(TVShow.NUM_SEASONS))
            tvShow = TVShow(id = id, popularity = popularity, voteCount = voteCount, duration = converter.getTVDuration(duration), originalLanguage = originalLanguage,
                originalName = originalName, genres = converter.getGenres(genres), name = name, status = status, number_of_seasons = numSeasons,
                score = score, overview = overview, firstAirDate = date)
        }
        return tvShow
    }
}