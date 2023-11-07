package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class ChatRoot(
    val SELLER_ID: String,
    val BUYER_ID: String,
    val PRODUCT_ID: String,
    val CREATED_AT: Timestamp
)
