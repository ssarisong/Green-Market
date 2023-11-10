package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.Chat
import kr.ac.hansung.greenmarket.models.ChatRoom
import kr.ac.hansung.greenmarket.utils.FirebaseChattingUtil
import java.text.SimpleDateFormat
import java.util.*

class ChatlistActivity : AppCompatActivity() {

    private lateinit var firebaseChattingUtil: FirebaseChattingUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        firebaseChattingUtil = FirebaseChattingUtil()

        // RecyclerView 설정
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_chatlist)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val chatRoomList = listOf(
            ChatRoom("product1", "user1", "seller1", Timestamp.now(), null, null),
            ChatRoom("product2", "user2", "seller2", Timestamp.now(), null, null),
            // Add more chat room items as needed
        )

        // 어댑터 설정
        val chatListAdapter = ChatListAdapter(chatRoomList) { clickedChatRoom ->
            // 클릭된 채팅 아이템의 정보를 받아옴
            // 여기에서 ChatActivity로 전환하는 Intent를 생성하고 시작
            firebaseChattingUtil.createChatRoom(
                clickedChatRoom.productId,
                clickedChatRoom.buyerId,
                clickedChatRoom.sellerId
            ) { status, chatRoomId ->
                if (status == StatusCode.SUCCESS) {
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("chatRoomId", chatRoomId)
                    startActivity(intent)
                } else {
                    // Handle failure case
                    Toast.makeText(this, "Failed to create chat room", Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = chatListAdapter
    }
}
