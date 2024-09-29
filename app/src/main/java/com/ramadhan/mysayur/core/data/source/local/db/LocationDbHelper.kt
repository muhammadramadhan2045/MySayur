package com.ramadhan.mysayur.core.data.source.local.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.TimeUnit

class LocationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Location.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_LOCATION = "location"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_TIMESTAMP = "timestamp"


        private  const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_LOCATION (" +
                "$COLUMN_LATITUDE REAL," +
                "$COLUMN_LONGITUDE REAL," +
                "$COLUMN_TIMESTAMP INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${LocationContract.LocationEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun saveLocation(latitude: Double, longitude: Double, timestamp: Long) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(LocationContract.LocationEntry.COLUMN_LATITUDE, latitude)
            put(LocationContract.LocationEntry.COLUMN_LONGITUDE, longitude)
            put(LocationContract.LocationEntry.COLUMN_TIMESTAMP, timestamp)
        }
        db.insert(TABLE_LOCATION, null, contentValues)
    }

    fun deleteOldData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_LOCATION")
    }

    fun isDataExist(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_LOCATION", null)
        val result = cursor.count > 0
        cursor.close()
        return result
    }
}
