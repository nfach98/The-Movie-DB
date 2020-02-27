package com.nf98.moviecatalogue.api.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class Movie(
    @SerializedName("popularity")
    @Expose
    var popularity: Float = 0.0f,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int = 0,

    @SerializedName("runtime")
    @Expose
    var duration: Int = 0,

    @SerializedName("video")
    @Expose
    var video: Boolean? = null,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,

    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null,

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null,

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null,

    @SerializedName("genres")
    @Expose
    var genres : List<Genre>? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("budget")
    @Expose
    var budget: Int = 0,

    @SerializedName("revenue")
    @Expose
    var revenue: Int = 0,

    @SerializedName("vote_average")
    @Expose
    var score: Float = 0.0f,

    @SerializedName("overview")
    @Expose
    var overview: String? = null,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
)