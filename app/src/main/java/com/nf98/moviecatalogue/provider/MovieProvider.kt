package com.nf98.moviecatalogue.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.database.MovieDatabase
import com.nf98.moviecatalogue.database.MovieDatabase.Companion.CONTENT_MOVIE
import com.nf98.moviecatalogue.database.MovieDatabase.Companion.CONTENT_TV
import com.nf98.moviecatalogue.database.MovieRepos

class MovieProvider : ContentProvider() {

    companion object {
        const val MOVIE = 0
        const val TV = 1

        const val MOVIE_ID = 2
        const val TV_ID = 3

        val URI_MOVIE: Uri = Uri.parse("content://com.nf98.moviecatalogue/MovieDB/$MOVIE" )
        val URI_TV: Uri = Uri.parse("content://com.nf98.moviecatalogue/MovieDB/$TV" )

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieRepos: MovieRepos
        private lateinit var movieDb: MovieDatabase
    }

    init {
        sUriMatcher.addURI("com.nf98.moviecatalogue", "movie", MOVIE)
        sUriMatcher.addURI("com.nf98.moviecatalogue", "tv_show", TV)

        sUriMatcher.addURI("com.nf98.moviecatalogue", "movie/#", MOVIE_ID)
        sUriMatcher.addURI("com.nf98.moviecatalogue", "tv_show/#", TV_ID)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var deleted = 0
            when (sUriMatcher.match(uri)) {
            MOVIE_ID ->{
                deleted = movieDb.movieDao().deleteMovie(ContentUris.parseId(uri))
                context?.contentResolver?.notifyChange(CONTENT_MOVIE, null)
            }
            TV_ID ->{
                deleted = movieDb.movieDao().deleteTVShow(ContentUris.parseId(uri))
                context?.contentResolver?.notifyChange(CONTENT_TV, null)
            }
        }

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long

        return when (sUriMatcher.match(uri)) {
            MOVIE -> {
                added = movieDb.movieDao().insertMovie(Movie.fromContentValues(values))
                context?.contentResolver?.notifyChange(CONTENT_MOVIE, null)
                Uri.parse("$CONTENT_MOVIE/$added")
            }
            TV -> {
                added = movieDb.movieDao().insertTVShow(TVShow.fromContentValues(values))
                context?.contentResolver?.notifyChange(CONTENT_TV, null)
                Uri.parse("$CONTENT_MOVIE/$added")
            }
            else -> throw IllegalArgumentException("Invalid")
        }
    }

    override fun onCreate(): Boolean {
        movieDb = MovieDatabase.getDatabase(context as Context)
        movieRepos = MovieRepos(movieDb.movieDao())
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val movieDao = MovieDatabase.getDatabase(context as Context).movieDao()
        return when (sUriMatcher.match(uri)) {
            MOVIE -> movieDao.getMovies()
            TV -> movieDao.getTVShows()
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
