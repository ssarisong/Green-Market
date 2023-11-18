package kr.ac.hansung.greenmarket.models

data class Product(
    val productId: String = "",
    val sellerId: String = "",
    val name: String = "",
    val img: String = "",
    val detail: String = "",
    val price: Int = 0,
    val stateCode: Int = 0
)