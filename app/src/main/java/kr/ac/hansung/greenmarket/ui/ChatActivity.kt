package kr.ac.hansung.greenmarket.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.Chat
import kr.ac.hansung.greenmarket.utils.FirebaseChattingUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var firebaseChattingUtil: FirebaseChattingUtil
    private lateinit var firebaseUserUtil: FirebaseUserUtil
    private lateinit var chatRoomId: String
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        firebaseUserUtil = FirebaseUserUtil()
        firebaseChattingUtil = FirebaseChattingUtil()

        chatRoomId = intent.getStringExtra("chatRoomId") ?: ""


        val btnQuit = findViewById<ImageButton>(R.id.btn_quit)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        val edtMessage = findViewById<EditText>(R.id.edt_message)
        val partnerName = findViewById<TextView>(R.id.txt_Title)

        recyclerView = findViewById(R.id.recycler_messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(mutableListOf(), firebaseUserUtil.whoAmI()?.uid ?: "")
        recyclerView.adapter = chatAdapter

        chatRoomId = intent.getStringExtra("chatRoomId") ?: ""

        firebaseChattingUtil.whoIsMyPartner(chatRoomId, firebaseUserUtil.whoAmI()?.uid ?: "") { partnerId ->
            if (partnerId != null) {
                firebaseUserUtil.getUser(partnerId) {STATUS_CODE, partner ->
                    if (STATUS_CODE == StatusCode.SUCCESS){
                        partnerName.text = partner?.name ?: ""
                    } else {
                        partnerName.text = ""
                    }
                }
            } else {
                partnerName.text = ""
            }
        }


        val messageListener = firebaseChattingUtil.listenForMessages(chatRoomId) { status, messages ->
            if (status == StatusCode.SUCCESS) {
                messages?.let {
                    chatAdapter.addChatList(it)
                    recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }
            } else {
                Toast.makeText(this, "Failed to listen for messages", Toast.LENGTH_SHORT).show()
            }
        }

        // 생명주기 이벤트 수신을 위해 LifecycleObserver를 구현한 익명 클래스를 생성
        val lifecycleObserver = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                messageListener.invoke()
            }
        }

        // 생명주기 이벤트 옵저버 등록
        lifecycle.addObserver(lifecycleObserver)

        btnSubmit.setOnClickListener {
            val message = edtMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                firebaseChattingUtil.sendMessage(firebaseUserUtil.whoAmI()?.uid ?: "", chatRoomId, message) { statusCode ->
                    if (statusCode == StatusCode.SUCCESS) {
                        edtMessage.text.clear()
                    } else {
                        Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnQuit.setOnClickListener {
            finish()
        }
    }
}