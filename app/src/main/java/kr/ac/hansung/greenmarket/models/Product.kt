package kr.ac.hansung.greenmarket.models

data class Product(
    val name: String,
    val img: String,
    val detail: String,
    val price: Int,
    val stateCode: Int
)