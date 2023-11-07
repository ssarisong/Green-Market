package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class ChatRoom(
    val sellerId: String,
    val buyerId: String,
    val productId: String,
    val createdAt: Timestamp
)
