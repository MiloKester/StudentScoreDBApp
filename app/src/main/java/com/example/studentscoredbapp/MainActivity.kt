package com.example.studentscoredbapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // declaring variables
        val btnAddScore : Button = findViewById(R.id.btnAddScore)
        val btnEditScore : Button = findViewById(R.id.btnEditScore)
        val btnDeleteScore : Button = findViewById(R.id.btnDeleteScore)
        val btnDeleteDB : Button = findViewById(R.id.btnDeleteDB)
        val subjectEntry : EditText = findViewById(R.id.editSubjectEntry) // EditText vs TextView
        val scoreEntry : EditText = findViewById(R.id.editScoreEntry)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        displayScores() // on load

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

            Toast.makeText(this, "$subject added to database", Toast.LENGTH_SHORT).show()

            subjectEntry.text.clear()
            scoreEntry.text.clear()
            displayScores()
        }

        btnEditScore.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = subjectEntry.text.toString()
            val score = scoreEntry.text.toString().toInt()
            val rows = db.updateScore(subject, score)

            Toast.makeText(this, "$rows score(s) updated", Toast.LENGTH_LONG).show()
            displayScores()
        }

        btnDeleteScore.setOnClickListener {
            // check if theres a subject in the textbox

            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.confirm_delete, null)

            val subject = subjectEntry.text.toString()

            dialogBuilder.setIcon(android.R.drawable.ic_delete)
            dialogBuilder.setTitle("Delete Score")
            dialogBuilder.setMessage("Are You Sure You Want To Delete Your $subject Score?\n\nThis Action is Permanent.")
            dialogBuilder.setView(dialogView)

            dialogBuilder.setPositiveButton("Yes") { _, _ ->
                    val db = DBHelper(this, null)

                    val rows = db.deleteScore(subject)

                    Toast.makeText(this,
                        when (rows) {
                            0 -> "Nothing deleted"
                            1 -> "1 score deleted"
                            else -> "" // shouldn't happen
                        },
                        Toast.LENGTH_LONG).show()

                    displayScores()
            }
            dialogBuilder.setNegativeButton("No", null)

            dialogBuilder.show()
        }

        btnDeleteDB.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.confirm_delete, null)

            dialogBuilder.setIcon(android.R.drawable.ic_delete)
            dialogBuilder.setTitle("Delete Database")
            dialogBuilder.setMessage("Are You Sure You Want To Delete The Entire Database?\n\nThis Action is Permanent.")
            dialogBuilder.setView(dialogView)

            dialogBuilder.setPositiveButton("Yes") { _, _ ->
                    val db = DBHelper(this, null)
                    val isSuccessful = db.deleteDB()

                    Toast.makeText(this,
                        when (isSuccessful) {
                            true -> "Database Successfully Deleted"
                            false -> "Failed to Delete Database"
                        },
                        Toast.LENGTH_LONG).show()

                    displayScores()
            }
            dialogBuilder.setNegativeButton("No", null)

            dialogBuilder.show()
        }

        /*
        recyclerView.layoutManager = LinearLayoutManager(this)
        val data = DBHelper(this, null)
        */

    }

    private fun displayScores() {
        val db = DBHelper(this, null)
        val scoreList = db.getScores()

        val scoreRecord : TextView = findViewById(R.id.tvScoreRecord)
        scoreRecord.text = getString(R.string.score_title) // clears by only having title

        scoreList.forEach {
            scoreRecord.append("$it\n")
        }
    }
}

// TODO: on item click, load into fields to edit or delete
// TODO: redesign score list to have proper columns
// ^ currently literally just a string with line breaks im gonna- recyclerview if i can figure it out
// TODO: transaction integrity

// find a better way to display scores
// 2 columns of Subject then Score?
// | SUBJECT | SCORE|
// | ENGLISH | 98   |
// table layout?

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

// ACID - atomicity, consistency, isolation, durability
