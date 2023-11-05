package kr.ac.hansung.greenmarket.models

import java.time.LocalDate

data class User(
    val EMAIL: String,
    val PASSWORD: String,
    val NAME: String,
    val BIRTHDAY: LocalDate,
    val JOIN_DATE: LocalDate,
)
