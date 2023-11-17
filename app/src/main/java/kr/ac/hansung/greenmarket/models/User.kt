package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class User(
    val email: String = "",
    val name: String = "",
    val birthday: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now()
)
