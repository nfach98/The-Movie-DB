package com.nf98.moviecatalogue.api.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
    var releaseDate: String? = null) : Parcelable {

    companion object{
        const val POPULARITY = "popularity"
        const val VOTE_COUNT = "vote_count"
        const val DURATION = "duration"
        const val ID = "id"
        const val ORI_LANGUAGE = "original_language"
        const val ORI_TITLE = "original_title"
        const val GENRES = "genres"
        const val TITLE = "title"
        const val STATUS = "status"
        const val BUDGET = "budget"
        const val REVENUE = "revenue"
        const val SCORE = "vote_average"
        const val OVERVIEW = "overview"
        const val RELEASE_DATE = "release_date"

        fun fromContentValues(@Nullable values: ContentValues?): Movie {
            val movie = Movie()
            if(values != null){
                if (values.containsKey(ID)) movie.id = values.getAsInteger(ID)
                if (values.containsKey(POPULARITY)) movie.popularity = values.getAsFloat(POPULARITY)
                if (values.containsKey(VOTE_COUNT)) movie.voteCount = values.getAsInteger(VOTE_COUNT)
                if (values.containsKey(DURATION)) movie.duration = values.getAsInteger(DURATION)
                if (values.containsKey(ORI_LANGUAGE)) movie.originalLanguage = values.getAsString(ORI_LANGUAGE)
                if (values.containsKey(ORI_TITLE)) movie.originalTitle = values.getAsString(ORI_TITLE)
                if (values.containsKey(GENRES)) movie.genres = values.get(GENRES) as List<Genre>
                if (values.containsKey(TITLE)) movie.title = values.getAsString(TITLE)
                if (values.containsKey(STATUS)) movie.status = values.getAsString(STATUS)
                if (values.containsKey(BUDGET)) movie.budget = values.getAsInteger(BUDGET)
                if (values.containsKey(REVENUE)) movie.revenue = values.getAsInteger(REVENUE)
                if (values.containsKey(SCORE)) movie.score = values.getAsFloat(SCORE)
                if (values.containsKey(OVERVIEW)) movie.overview = values.getAsString(OVERVIEW)
                if (values.containsKey(RELEASE_DATE)) movie.releaseDate = values.getAsString(RELEASE_DATE)
            }
            return movie
        }
    }
}