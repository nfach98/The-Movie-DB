package com.nf98.moviecatalogue.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TVShow (
    @SerializedName("original_name")
    @Expose
    var originalName: String? = null,

    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("popularity")
    @Expose
    var popularity: Float = 0f,

    @SerializedName("origin_country")
    @Expose
    var originCountry: List<String>? = null,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int = 0,

    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String? = null,

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null,

    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("vote_average")
    @Expose
    var score: Float = 0f,

    @SerializedName("overview")
    @Expose
    var overview: String? = null,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null )