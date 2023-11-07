package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class Chat(
    val chatroomId: String,
    val senderId: String,
    val message: String,
    val createdAt: Timestamp
)
