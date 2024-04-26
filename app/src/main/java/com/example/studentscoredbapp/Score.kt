package com.example.studentscoredbapp

class Score (private val subject:String, private val score: Int) {
    override fun toString(): String { // return the record detail
        return "$subject($score)"
    }
}