package com.nf98.moviecatalogue.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.database.MovieDatabase
import com.nf98.moviecatalogue.database.MovieDatabase.Companion.CONTENT_MOVIE
import com.nf98.moviecatalogue.database.MovieDatabase.Companion.CONTENT_TV
import com.nf98.moviecatalogue.database.MovieRepos
import kotlinx.coroutines.*

class MovieProvider : ContentProvider() {

    companion object {
        const val MOVIE = 0
        const val TV = 1

        const val MOVIE_ID = 2
        const val TV_ID = 3

        val URI_MOVIE: Uri = Uri.parse("content://com.nf98.moviecatalogue/movie/$MOVIE" )
        val URI_TV: Uri = Uri.parse("content://com.nf98.moviecatalogue/tv_show/$TV" )

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
                GlobalScope.launch{
                    val deferred = async(Dispatchers.IO){ movieDb.movieDao().deleteMovie(ContentUris.parseId(uri)) }
                    deleted = deferred.await()
                }
                context?.contentResolver?.notifyChange(CONTENT_MOVIE, null)
            }
            TV_ID ->{
                GlobalScope.launch{
                    val deferred = async(Dispatchers.IO){ movieDb.movieDao().deleteTVShow(ContentUris.parseId(uri)) }
                    deleted = deferred.await()
                }
                context?.contentResolver?.notifyChange(CONTENT_TV, null)
            }
        }

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var added = 0L

        return when (sUriMatcher.match(uri)) {
            MOVIE -> {
                GlobalScope.launch {
                    val deferred = async(Dispatchers.IO){ movieDb.movieDao().insertMovie(Movie.fromContentValues(values)) }
                    added = deferred.await()
                }
                context?.contentResolver?.notifyChange(CONTENT_MOVIE, null)
                Uri.parse("$CONTENT_MOVIE/$added")
            }
            TV -> {
                GlobalScope.launch {
                    val deferred = async(Dispatchers.IO){ movieDb.movieDao().insertTVShow(TVShow.fromContentValues(values)) }
                    added = deferred.await()
                }
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
        var cursor: Cursor? = null
        return when (sUriMatcher.match(uri)) {
            MOVIE -> {
                GlobalScope.launch {
                    val deferred = async(Dispatchers.IO) { movieDao.getMovies() }
                    cursor = deferred.await()
                }
                cursor
            }
            TV -> {
                GlobalScope.launch {
                    val deferred = async(Dispatchers.IO) { movieDao.getTVShows() }
                    cursor = deferred.await()
                }
                cursor
            }
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
