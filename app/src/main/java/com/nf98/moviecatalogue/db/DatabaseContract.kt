package com.nf98.moviecatalogue.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MovieColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie"

            const val ORDER = "rowid"
            const val POPULARITY = "popularity"
            const val VOTE_COUNT = "vote_count"
            const val DURATION = "duration"
            const val VIDEO = "video"
            const val POSTER_PATH = "poster_path"
            const val ID = "_id"
            const val ADULT = "adult"
            const val BACKDROP_PATH = "backdrop_path"
            const val ORI_LANGUAGE = "original_language"
            const val ORI_TITLE = "original_title"
            const val GENRES = "genres_id"
            const val TITLE = "title"
            const val STATUS = "status"
            const val BUDGET = "budget"
            const val REVENUE = "revenue"
            const val SCORE = "score"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
        }
    }

    internal class TVShowColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "tv_show"

            const val ORDER = "rowid"
            const val RUNTIME = "episode_run_time"
            const val ORI_NAME = "original_name"
            const val GENRES = "genres_id"
            const val NUMBER_SEASON = "number_of_seasons"
            const val NAME = "name"
            const val POPULARITY = "popularity"
            const val VOTE_COUNT = "vote_count"
            const val FIRST_AIR_DATE = "first_air_date"
            const val BACKDROP_PATH = "backdrop_path"
            const val ORI_LANGUAGE = "original_language"
            const val ID = "id"
            const val SCORE = "score"
            const val OVERVIEW = "overview"
            const val POSTER_PATH = "poster_path"
            const val STATUS = "status"
        }
    }

}