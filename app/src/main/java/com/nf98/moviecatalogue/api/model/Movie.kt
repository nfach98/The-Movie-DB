package com.nf98.moviecatalogue.api.model

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie(
    @SerializedName("popularity")
    @Expose
    @ColumnInfo(name = "popularity")
    var popularity: Float = 0.0f,

    @SerializedName("vote_count")
    @Expose
    @ColumnInfo(name = "vote_count")
    var voteCount: Int = 0,

    @SerializedName("runtime")
    @Expose
    @ColumnInfo(name = "duration")
    var duration: Int = 0,

    @SerializedName("video")
    @Expose
    var video: Boolean? = null,

    @SerializedName("poster_path")
    @Expose
    @Ignore
    var posterPath: String? = null,

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null,

    @SerializedName("backdrop_path")
    @Expose
    @Ignore
    var backdropPath: String? = null,

    @SerializedName("original_language")
    @Expose
    @ColumnInfo(name = "original_language")
    var originalLanguage: String? = null,

    @SerializedName("original_title")
    @Expose
    @ColumnInfo(name = "original_title")
    var originalTitle: String? = null,

    @SerializedName("genres")
    @Expose
    @ColumnInfo(name = "genres")
    var genres : List<Genre>? = null,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String? = null,

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    var status: String? = null,

    @SerializedName("budget")
    @Expose
    @ColumnInfo(name = "budget")
    var budget: Int = 0,

    @SerializedName("revenue")
    @Expose
    @ColumnInfo(name = "revenue")
    var revenue: Int = 0,

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "vote_average")
    var score: Float = 0.0f,

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "release_date")
    var releaseDate: String? = null) : Parcelable