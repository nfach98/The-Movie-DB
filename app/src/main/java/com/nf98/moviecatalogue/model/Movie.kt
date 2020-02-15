package com.nf98.moviecatalogue.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("popularity")
    @Expose
    var popularity: Float = 0.0f,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int = 0,

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

    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("vote_average")
    @Expose
    var score: Float = 0.0f,

    @SerializedName("overview")
    @Expose
    var overview: String? = null,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null )