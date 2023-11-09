package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class ChatRoom(
    val productId: String,
    val buyerId: String,
    val sellerId: String,
    val createdAt: Timestamp,
    val lastMessageAt: Timestamp? = null,
    val lastMessage: String? = null
)
