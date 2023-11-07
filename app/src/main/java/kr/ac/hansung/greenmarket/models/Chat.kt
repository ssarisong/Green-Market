package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class Chat(
    val chatroom_id: String,
    val sender_id: String,
    val message: String,
    val created_at: Timestamp
)
