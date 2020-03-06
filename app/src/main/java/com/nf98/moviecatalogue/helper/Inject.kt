package com.nf98.moviecatalogue.helper

import android.content.Context
import com.nf98.moviecatalogue.app.ViewModelFactory
import com.nf98.moviecatalogue.database.MovieDatabase
import com.nf98.moviecatalogue.database.MovieRepos

object Inject {

    lateinit var movieDb: MovieDatabase

    private fun provideMovieRepos(context: Context): MovieRepos {
        movieDb = MovieDatabase.getDatabase(context)
        return MovieRepos(movieDb.movieDao())
    }

    fun closeDatabase(){
        movieDb.close()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val repos = provideMovieRepos(context)
        return ViewModelFactory(repos)
    }
}