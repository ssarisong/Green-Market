package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class Chat(
    val CHATROOM_ID: String,
    val SENDER_ID: String,
    val MESSAGE: String,
    val CREATED_AT: Timestamp
)
