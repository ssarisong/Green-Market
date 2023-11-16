package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class ChatRoom(
    val chatRoomId: String = "",
    val productId: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val lastMessageAt: Timestamp? = null,
    val lastMessage: String? = null
)

