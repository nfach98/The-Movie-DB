package com.nf98.moviecatalogue.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nf98.moviecatalogue.api.ApiMain
import com.nf98.moviecatalogue.api.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    val movie = MutableLiveData<Movie>()

    internal fun getMovie(id: Int): LiveData<Movie> {
        val result = ApiMain().services.getMovie(id)

        result.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if(response.isSuccessful)
                    response.body().let { movie.postValue(it) }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("MovieDB: ", t.message)
            }
        })
        return movie
    }

}