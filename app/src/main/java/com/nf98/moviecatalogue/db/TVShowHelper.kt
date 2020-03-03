package com.nf98.moviecatalogue.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.nf98.moviecatalogue.db.DatabaseContract.TVShowColumns.Companion.ID
import com.nf98.moviecatalogue.db.DatabaseContract.TVShowColumns.Companion.ORDER
import com.nf98.moviecatalogue.db.DatabaseContract.TVShowColumns.Companion.TABLE_NAME

class TVShowHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: TVShowHelper? = null

        fun getInstance(context: Context): TVShowHelper = INSTANCE ?: synchronized(this) { INSTANCE ?: TVShowHelper(context) }
    }

    @Throws(SQLException::class)
    fun open() { database = databaseHelper.writableDatabase }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$ORDER ASC",
            null)
    }

    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$ID = ?", arrayOf(id), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
    }

    fun deleteById(id: Int): Int {
        return database.delete(DATABASE_TABLE, "$ID = $id", null)
    }
}