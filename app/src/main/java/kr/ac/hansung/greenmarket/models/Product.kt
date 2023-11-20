package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp

data class Product(
    val productId: String = "",
    val sellerId: String = "",
    val name: String = "",
    val img: String = "",
    val detail: String = "",
    val price: Int = 0,
    val stateCode: Int = 0,
    val createdAt: Timestamp = Timestamp.now()
)