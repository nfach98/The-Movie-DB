package com.nf98.moviecatalogue.response

import com.google.gson.annotations.SerializedName
import com.nf98.moviecatalogue.model.TVShow

class TVShowResponse (
    @SerializedName("results")
    val tvShows: ArrayList<TVShow>
)