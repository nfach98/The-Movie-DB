package com.nf98.moviecatalogue.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "themoviedb"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE ${DatabaseContract.MovieColumns.TABLE_NAME}" +
                " (${DatabaseContract.MovieColumns.ID} INTEGER PRIMARY KEY," +
                " ${DatabaseContract.MovieColumns.POPULARITY} REAL," +
                " ${DatabaseContract.MovieColumns.VOTE_COUNT} INTEGER," +
                " ${DatabaseContract.MovieColumns.DURATION} INTEGER," +
                " ${DatabaseContract.MovieColumns.VIDEO} INTEGER," +
                " ${DatabaseContract.MovieColumns.POSTER_PATH} TEXT," +
                " ${DatabaseContract.MovieColumns.ADULT} INTEGER," +
                " ${DatabaseContract.MovieColumns.BACKDROP_PATH} TEXT," +
                " ${DatabaseContract.MovieColumns.ORI_LANGUAGE} TEXT," +
                " ${DatabaseContract.MovieColumns.ORI_TITLE} TEXT," +
                " ${DatabaseContract.MovieColumns.GENRES} TEXT," +
                " ${DatabaseContract.MovieColumns.TITLE} TEXT," +
                " ${DatabaseContract.MovieColumns.STATUS} TEXT," +
                " ${DatabaseContract.MovieColumns.BUDGET} INTEGER," +
                " ${DatabaseContract.MovieColumns.REVENUE} INTEGER," +
                " ${DatabaseContract.MovieColumns.SCORE} REAL," +
                " ${DatabaseContract.MovieColumns.OVERVIEW} TEXT," +
                " ${DatabaseContract.MovieColumns.RELEASE_DATE} TEXT" +")"

        private const val SQL_CREATE_TABLE_TV_SHOW = "CREATE TABLE ${DatabaseContract.TVShowColumns.TABLE_NAME}" +
                " (${DatabaseContract.TVShowColumns.ID} INTEGER PRIMARY KEY," +
                " ${DatabaseContract.TVShowColumns.RUNTIME} TEXT," +
                " ${DatabaseContract.TVShowColumns.ORI_NAME} TEXT," +
                " ${DatabaseContract.TVShowColumns.GENRES} TEXT," +
                " ${DatabaseContract.TVShowColumns.NUMBER_SEASON} INTEGER," +
                " ${DatabaseContract.TVShowColumns.NAME} TEXT," +
                " ${DatabaseContract.TVShowColumns.POPULARITY} REAL," +
                " ${DatabaseContract.TVShowColumns.VOTE_COUNT} INTEGER," +
                " ${DatabaseContract.TVShowColumns.FIRST_AIR_DATE} TEXT," +
                " ${DatabaseContract.TVShowColumns.BACKDROP_PATH} TEXT," +
                " ${DatabaseContract.TVShowColumns.ORI_LANGUAGE} TEXT," +
                " ${DatabaseContract.TVShowColumns.SCORE} REAL," +
                " ${DatabaseContract.TVShowColumns.OVERVIEW} TEXT," +
                " ${DatabaseContract.TVShowColumns.POSTER_PATH} TEXT" +
                " ${DatabaseContract.TVShowColumns.STATUS} TEXT" +")"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
        db.execSQL(SQL_CREATE_TABLE_TV_SHOW)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MovieColumns.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TVShowColumns.TABLE_NAME}")
        onCreate(db)
    }

}