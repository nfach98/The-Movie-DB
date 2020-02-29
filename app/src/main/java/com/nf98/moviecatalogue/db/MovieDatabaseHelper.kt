package com.nf98.moviecatalogue.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class MovieDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MovieColumns.TABLE_NAME}")
        onCreate(db)
    }

}