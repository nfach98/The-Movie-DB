package com.nf98.moviecatalogue.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nf98.moviecatalogue.api.model.Genre
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun convertGenres(genres: List<Genre>?): String? {
        return if (genres != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Genre>?>() {}.type
            gson.toJson(genres, type)
        } else null
    }

    @TypeConverter
    fun getGenres(genreString: String?): List<Genre>? {
        return if (genreString != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Genre?>?>() {}.type
            gson.fromJson<List<Genre>>(genreString, type)
        } else null
    }

    @TypeConverter
    fun convertTVDuration(durations: List<Int>?): String? {
        return if (durations != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Int>?>() {}.type
            gson.toJson(durations, type)
        } else null
    }

    @TypeConverter
    fun getTVDuration(durationString: String?): List<Int>? {
        return if (durationString != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Int?>?>() {}.type
            gson.fromJson<List<Int>>(durationString, type)
        } else null
    }
}