package kr.ac.hansung.greenmarket.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.greenmarket.R
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.Timestamp
import kr.ac.hansung.greenmarket.models.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView // Declare recyclerView as a member variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) // activity_chat.xml을 연결


        val btnQuit = findViewById<ImageButton>(R.id.btn_quit)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        val edtMessage = findViewById<EditText>(R.id.edt_message)

        recyclerView = findViewById(R.id.recycler_messages) // Initialize recyclerView here

        btnQuit.setOnClickListener { // 이전 버튼
            finish()
        }

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(mutableListOf()) // 초기에 빈 리스트로 설정
        recyclerView.adapter = chatAdapter

        btnSubmit.setOnClickListener {
            // 전송 버튼 클릭 시 호출되는 함수
            val message = edtMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                val currentTime = getCurrentTime()
                val chatItem = Chat("user1", message, currentTime)
                addMessage(chatItem)
                edtMessage.text.clear() // 메시지 전송 후 입력창 초기화
            }
        }
    }

    private fun getCurrentTime(): Timestamp {
        // 현재 시간을 가져와서 Timestamp로 변환
        val currentTime = Calendar.getInstance().time
        return Timestamp(currentTime)
    }

    private fun addMessage(chatItem: Chat) {
        // RecyclerView에 메시지 추가
        chatAdapter.addChat(chatItem)
        // 스크롤을 가장 최근 메시지로 이동
        recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }
}
