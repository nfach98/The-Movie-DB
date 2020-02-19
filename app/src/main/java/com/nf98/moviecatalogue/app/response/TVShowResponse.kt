package com.nf98.moviecatalogue.app.response

import com.google.gson.annotations.SerializedName
import com.nf98.moviecatalogue.api.model.TVShow

class TVShowResponse (
    @SerializedName("results")
    val tvShows: ArrayList<TVShow>
)