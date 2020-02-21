package com.nf98.moviecatalogue.api.response

import com.google.gson.annotations.SerializedName
import com.nf98.moviecatalogue.api.model.Credit

data class CreditsResponse (
    @SerializedName("cast")
    val cast: ArrayList<Credit>,

    @SerializedName("crew")
    val crew: ArrayList<Credit>
)