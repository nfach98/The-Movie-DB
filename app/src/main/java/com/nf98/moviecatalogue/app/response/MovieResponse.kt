package com.nf98.moviecatalogue.app.response

import com.google.gson.annotations.SerializedName
import com.nf98.moviecatalogue.api.model.Movie

data class MovieResponse (
    @SerializedName("results")
    val movies: ArrayList<Movie>
)