package com.nf98.moviecatalogue.api.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "tv_show")
data class TVShow (
    @SerializedName("episode_run_time")
    @Expose
    @Ignore
    var duration : List<Int>? = null,

    @SerializedName("original_name")
    @Expose
    @ColumnInfo(name = "original_name")
    var originalName: String? = null,

    @SerializedName("genres")
    @Expose
    @Ignore
    var genres : @RawValue List<Genre>? = null,

    @SerializedName("genre_ids")
    @Expose
    @Ignore
    var genreIds: List<Int>? = null,

    @SerializedName("number_of_seasons")
    @Expose
    @ColumnInfo(name = "number_of_seasons")
    var number_of_seasons : Int = 0,

    @SerializedName("seasons")
    @Expose
    @Ignore
    var seasons : @RawValue List<Season>? = null,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null,

    @SerializedName("popularity")
    @Expose
    @ColumnInfo(name = "popularity")
    var popularity: Float = 0f,

    @SerializedName("origin_country")
    @Expose
    @Ignore
    var originCountry: List<String>? = null,

    @SerializedName("vote_count")
    @Expose
    @ColumnInfo(name = "vote_count")
    var voteCount: Int = 0,

    @SerializedName("first_air_date")
    @Expose
    @ColumnInfo(name = "first_air_date")
    var firstAirDate: String? = null,

    @SerializedName("backdrop_path")
    @Expose
    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("original_language")
    @Expose
    @ColumnInfo(name = "original_language")
    var originalLanguage: String? = null,

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "vote_average")
    var score: Float = 0f,

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "poster_path")
    var posterPath: String? = null,

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    var status : String? = null) : Parcelable