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
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "tv_show")
data class TVShow (
    @SerializedName("episode_run_time")
    @Expose
    @ColumnInfo(name = "run_times")
    var duration : List<Int>? = null,

    @SerializedName("original_name")
    @Expose
    @ColumnInfo(name = "original_name")
    var originalName: String? = null,

    @SerializedName("genres")
    @Expose
    @ColumnInfo(name = "genres")
    var genres : List<Genre>? = null,

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
    @Ignore
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
    @Ignore
    var posterPath: String? = null,

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    var status : String? = null) : Parcelable {

    companion object{
        const val RUNTIMES = "run_times"
        const val ORI_NAME = "original_name"
        const val GENRES = "genres"
        const val NUM_SEASONS = "number_of_seasons"
        const val NAME = "name"
        const val POPULARITY = "popularity"
        const val VOTE_COUNT = "vote_count"
        const val FIRST_AIR_DATE = "first_air_date"
        const val ORI_LANGUAGE = "original_language"
        const val ID = "id"
        const val SCORE = "vote_average"
        const val OVERVIEW = "overview"
        const val STATUS = "status"

        fun fromContentValues(@Nullable values: ContentValues?): TVShow {
            val tvShow = TVShow()
            if(values != null){
                if (values.containsKey(ID)) tvShow.id = values.getAsInteger(ID)
                if (values.containsKey(POPULARITY)) tvShow.popularity = values.getAsFloat(POPULARITY)
                if (values.containsKey(VOTE_COUNT)) tvShow.voteCount = values.getAsInteger(VOTE_COUNT)
                if (values.containsKey(NUM_SEASONS)) tvShow.number_of_seasons = values.getAsInteger(NUM_SEASONS)
                if (values.containsKey(RUNTIMES)) tvShow.duration = values.get(RUNTIMES) as List<Int>
                if (values.containsKey(ORI_LANGUAGE)) tvShow.originalLanguage = values.getAsString(ORI_LANGUAGE)
                if (values.containsKey(ORI_NAME)) tvShow.originalName = values.getAsString(ORI_NAME)
                if (values.containsKey(GENRES)) tvShow.genres = values.get(GENRES) as List<Genre>
                if (values.containsKey(NAME)) tvShow.name = values.getAsString(NAME)
                if (values.containsKey(STATUS)) tvShow.status = values.getAsString(STATUS)
                if (values.containsKey(SCORE)) tvShow.score = values.getAsFloat(SCORE)
                if (values.containsKey(OVERVIEW)) tvShow.overview = values.getAsString(OVERVIEW)
                if (values.containsKey(FIRST_AIR_DATE)) tvShow.firstAirDate = values.getAsString(FIRST_AIR_DATE)
            }
            return tvShow
        }
    }
}