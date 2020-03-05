package com.nf98.moviecatalogue.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nf98.moviecatalogue.database.MovieRepos

class ViewModelFactory(private val movieRepos: MovieRepos): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MovieRepos::class.java).newInstance(movieRepos)
    }
}