package com.nf98.moviecatalogue.helper

import android.content.Context
import com.nf98.moviecatalogue.app.main.MainViewModelFactory
import com.nf98.moviecatalogue.database.MovieDatabase
import com.nf98.moviecatalogue.database.MovieRepos

object Inject {
    fun provideMovieRepos(context: Context): MovieRepos {
        val movieDb = MovieDatabase.getDatabase(context)
        return MovieRepos(movieDb.movieDao())
    }

    fun provideViewModelFactory(context: Context): MainViewModelFactory {
        val repos = provideMovieRepos(context)
        return MainViewModelFactory(repos)
    }
}