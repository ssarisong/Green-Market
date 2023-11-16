package kr.ac.hansung.greenmarket.models

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Chat(
    val senderId: String,
    val message: String,
    val createdAt: Timestamp
) {
    fun getFormattedDate(): String {
        // Timestamp를 가져와서 HH:mm 형식으로 변환
        val formattedDate = SimpleDateFormat("HH:mm", Locale.getDefault()).format(createdAt.toDate())
        return formattedDate
    }
}
