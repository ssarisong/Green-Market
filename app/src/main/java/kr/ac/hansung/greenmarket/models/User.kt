package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class User(
    val EMAIL: String,
    val PASSWORD: String,
    val NAME: String,
    val BIRTHDAY: Timestamp,
    val JOIN_DATE: Timestamp,
)
