package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp
import java.util.Date

data class User(
    val email: String = "",
    val name: String = "",
    val birthday: Timestamp = Timestamp(Date(0, 0, 1)), // 입력 안들어오면 1900-01-01로 설정됨
    val createdAt: Timestamp = Timestamp.now()
)
