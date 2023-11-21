package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    val senderId: String = "",
    val message: String = "",
    val createdAt: Timestamp = Timestamp.now()
)