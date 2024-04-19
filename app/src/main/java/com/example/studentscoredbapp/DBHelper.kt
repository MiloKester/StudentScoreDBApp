package com.example.studentscoredbapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    var context: Context
    init {
        this.context = context
    }

    companion object {
        private val DB_NAME = "student_management"
        private val DB_VERSION = 1
        val TABLE_NAME = "student_score"
        val ID = "id"
        val SUBJECT = "subject"
        val SCORE = "score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY," +
                    "$SUBJECT TEXT," +
                    "$SCORE INTEGER" + ")"
                )
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addScore(subject: String, score: Int) {
        val values = ContentValues()

        values.put(SUBJECT, subject)
        values.put(SCORE, score)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    /*
    fun getScores(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }
     */

    fun getScores() : ArrayList<Score> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        val scoreList = ArrayList<Score>()

        if (cursor.moveToFirst()) {
            do {
                scoreList.add(
                    Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SUBJECT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SCORE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return scoreList
    }

    fun deleteScore(subject: String) : Int {
        val db = this.writableDatabase

        val rows = db.delete(TABLE_NAME, "subject=?", arrayOf(subject))
        db.close()

        return rows
    }

    fun deleteDB(): Boolean {
        return context.deleteDatabase(DB_NAME)
    }

    fun updateScore(subject: String, score: Int): Int {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(SCORE, score)

        val rows = db.update(TABLE_NAME, values, "subject=?", arrayOf(subject))
        db.close()

        return rows
    }

    fun recreateDatabaseAndTables() {

    }
}
