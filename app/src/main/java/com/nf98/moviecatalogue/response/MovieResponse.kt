package com.nf98.moviecatalogue.response

import com.google.gson.annotations.SerializedName
import com.nf98.moviecatalogue.model.Movie

data class MovieResponse (
    @SerializedName("results")
    val movies: ArrayList<Movie>
)