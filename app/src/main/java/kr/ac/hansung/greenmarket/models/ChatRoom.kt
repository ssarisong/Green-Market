package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class ChatRoom(
    val seller_id: String,
    val buyer_id: String,
    val product_id: String,
    val created_at: Timestamp
)
