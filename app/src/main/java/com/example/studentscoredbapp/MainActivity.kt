package com.example.studentscoredbapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Log

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // declaring variables
        val btnAddScore : Button = findViewById(R.id.btnAddScore)
        val btnEditScore : Button = findViewById(R.id.btnEditScore)
        val btnDeleteScore : Button = findViewById(R.id.btnDeleteScore)
        val btnDeleteDB : Button = findViewById(R.id.btnDeleteDB)
        val btnDisplayScore : Button = findViewById(R.id.btnDisplayScore)
        val subjectEntry : EditText = findViewById(R.id.editSubjectEntry) // EditText vs TextView
        val scoreEntry : EditText = findViewById(R.id.editScoreEntry)

        btnAddScore.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = subjectEntry.text.toString() // theres various ways of converting to both string and int so research those and find whats best
            val score = scoreEntry.text.toString().toInt()

            // add error handling for if subject exists already
            // for each in database column
            // if row == subject
            // display toast notif and break
            // if not add to database
            db.addScore(subject, score) // error i think as a result of db line

            Toast.makeText(this, subject + " added to database", Toast.LENGTH_SHORT).show()

            subjectEntry.text.clear()
            scoreEntry.text.clear()
            // redisplay
        }

        btnEditScore.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = subjectEntry.text.toString()
            val score = scoreEntry.text.toString().toInt()
            val rows = db.updateScore(subject, score)

            Toast.makeText(this, "$rows score(s) updated", Toast.LENGTH_LONG).show()
        }

        btnDeleteScore.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = subjectEntry.text.toString()
            val rows = db.deleteScore(subject)

            Toast.makeText(this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 score deleted"
                    else -> "" // shouldn't happen
                },
                Toast.LENGTH_LONG).show()
        }

        btnDeleteDB.setOnClickListener {
            val db = DBHelper(this, null)
            val isSuccessful = db.deleteDB()

            // popup warning confirmation should go here

            Toast.makeText(this,
                when (isSuccessful) {
                    true -> "Database Successfully Deleted"
                    false -> "Failed to Delete Database"
                },
                Toast.LENGTH_LONG).show()
        }

        /*
        btnDisplayScore.setOnClickListener {

            val db = DBHelper(this, null)
            val cursor = db.getScores()

            cursor!!.moveToFirst()
            val scoreRecord : TextView = findViewById(R.id.tvScoreRecord)

            scoreRecord.text = "Scores:\n" // clears by only having title

            if (cursor!!.moveToFirst()) {
                scoreRecord.append(cursor.getString(0) + ":" +
                cursor.getString(1) +
                "(" + cursor.getString(2) + ")\n")
            }

                while (cursor.moveToNext()) {
                scoreRecord.append(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.ID)) +
                        ": " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT)) +
                        " (" + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SCORE)) + ")\n")
            }
        }
         */

        btnDisplayScore.setOnClickListener {
            val db = DBHelper(this, null)
            val scoreList = db.getScores()

            val scoreRecord : TextView = findViewById(R.id.tvScoreRecord)
            scoreRecord.text = getString(R.string.score_title) // clears by only having title

            scoreList.forEach {
                scoreRecord.append("$it\n")
            }
        }
    }
}

// transaction integrity through the confirmation thingy

// Database Transaction Integrity
//• Consider a banking application that transfers funds from a savings account to a checking account. If the
//application successfully subtracts the funds from the savings account, but is interrupted by a system problem
//before it can add the funds to the checking account, the money would disappear, creating unbalanced bank
//accounts.
//• The two update commands (subtracting and adding funds) must be described as parts of a single logical
//transaction, so that the subtraction and addition updates are not written to the data source independently of
//each other.
//• Vulnerable Transaction Integrity example

// that shit ^