package com.nf98.moviecatalogue.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.database.MovieDatabase
import com.nf98.moviecatalogue.database.MovieDatabase.Companion.CONTENT_URI
import com.nf98.moviecatalogue.database.MovieRepos
import com.nf98.moviecatalogue.helper.Inject

class MovieProvider : ContentProvider() {

    companion object {
        const val MOVIE = 0
        const val TV = 1

        val URI_MOVIE = Uri.parse("content://com.nf98.moviecatalogue/fav/$MOVIE" )
        val URI_TV = Uri.parse("content://com.nf98.moviecatalogue/fav/$TV" )

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieRepos: MovieRepos
        private lateinit var movieDb: MovieDatabase
    }

    init {
        sUriMatcher.addURI("com.nf98.moviecatalogue", "fav", MOVIE)
        sUriMatcher.addURI("com.nf98.moviecatalogue", "fav", TV)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (sUriMatcher.match(uri)) {
            MOVIE -> movieDb.movieDao().deleteMovie(ContentUris.parseId(uri))
            TV -> movieDb.movieDao().deleteTVShow(ContentUris.parseId(uri))
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long =
            when (sUriMatcher.match(uri)) {
                MOVIE -> movieDb.movieDao().insertMovie(Movie.fromContentValues(values))
                TV -> movieDb.movieDao().insertTVShow(TVShow.fromContentValues(values))
                else -> 0
            }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
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
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
