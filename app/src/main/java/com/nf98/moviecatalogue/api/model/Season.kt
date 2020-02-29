package com.nf98.moviecatalogue.api.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Season (
    @SerializedName("air_date")
    @Expose
    var air_date : String? = null,

    @SerializedName("episode_count")
    @Expose
    var episode_count : Int = 0,

    @SerializedName("id")
    @Expose
    var id : Int = 0,

    @SerializedName("name")
    @Expose
    var name : String? = null,

    @SerializedName("overview")
    @Expose
    var overview : String? = null,

    @SerializedName("poster_path")
    @Expose
    var poster_path : String? = null,

    @SerializedName("season_number")
    @Expose
    var season_number : Int = 0
) : Parcelable