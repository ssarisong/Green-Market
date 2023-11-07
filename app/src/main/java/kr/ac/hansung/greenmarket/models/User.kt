package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class User(
    val email: String,
    //val password: String,
    val name: String,
    val birthday: Timestamp,
    val created_at: Timestamp,
)
