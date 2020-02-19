package com.nf98.moviecatalogue.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Genre (
    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("name")
    @Expose
    var name: String? = null
)