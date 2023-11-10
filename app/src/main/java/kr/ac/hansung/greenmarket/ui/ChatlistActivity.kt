package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.ChatRoom
import kr.ac.hansung.greenmarket.utils.FirebaseChattingUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class ChatlistActivity : AppCompatActivity() {

    private lateinit var chattingUtil: FirebaseChattingUtil
    private lateinit var userUtil: FirebaseUserUtil
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private var chatRoomList = mutableListOf<ChatRoom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlist)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNav.selectedItemId = R.id.chatting

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.mypage -> {
                    startActivity(Intent(this, MypageActivity::class.java))
                    true
                }
                else -> false
            }
        }

        chattingUtil = FirebaseChattingUtil()
        userUtil = FirebaseUserUtil()

        setupRecyclerView()
        loadChatRooms()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recycler_chatlist)
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatListAdapter = ChatListAdapter(chatRoomList) { clickedChatRoom ->
            // 채팅방 클릭 시의 행동을 정의
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatRoomId", clickedChatRoom.chatRoomId)
            startActivity(intent)
        }
        recyclerView.adapter = chatListAdapter
    }

    private fun loadChatRooms() {
        userUtil.whoAmI()?.let { user ->
            chattingUtil.getUserChatRooms(user.uid) { statusCode, chatRooms ->
                // UI 작업을 메인 스레드로 전환
                CoroutineScope(Dispatchers.Main).launch {
                    if (statusCode == StatusCode.SUCCESS) {
                        chatRoomList.clear()
                        chatRooms?.let { chatRoomList.addAll(it) }
                        chatListAdapter.notifyDataSetChanged()
                    } else {
                        // 실패 처리
                    }
                }
            }
        }
    }

}