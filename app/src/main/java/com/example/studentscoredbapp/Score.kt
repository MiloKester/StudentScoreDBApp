package com.example.studentscoredbapp

class Score (var id: Int, val subject:String , val score: Int) {
    override fun toString(): String { // return the record detail
        return "$id: $subject($score)"
    }
}