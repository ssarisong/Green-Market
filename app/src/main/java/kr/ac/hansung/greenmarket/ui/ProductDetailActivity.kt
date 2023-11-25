package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseChattingUtil
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val userUtil = FirebaseUserUtil()
        val productUtil = FirebaseProductUtil()
        val chatUtil = FirebaseChattingUtil()

        // 클릭한 제품의 정보 받아오기
        val productId = intent.getStringExtra("productId")?:""

        if (productId != null) {
            productUtil.getProductById(productId) { STATUS_CODE, product ->
                if(STATUS_CODE==StatusCode.SUCCESS){
                    findViewById<TextView>(R.id.tv_title2).text = product?.name ?: "로딩 실패"
                    findViewById<TextView>(R.id.tv_product_detail2).text = product?.detail ?: "로딩 실패"
                    findViewById<TextView>(R.id.tv_price2).text = product?.price.toString()
                    Glide.with(this)
                        .load(product?.img)
                        .into(findViewById<ImageView>(R.id.img_main))

                    val productStatusTextView = findViewById<TextView>(R.id.tv_product_status)
                    val statusString = getProductStatusString(product?.stateCode ?: 0)
                    productStatusTextView.text = statusString
                }
            }
        }
        val quitButton: ImageButton = findViewById(R.id.btn_quit)
        val chatButton: Button = findViewById(R.id.btn_chatting)

        quitButton.setOnClickListener {// 이전 버튼
            startActivity(Intent(this, HomeActivity::class.java))
        }
        chatButton.setOnClickListener {  // 채팅하기 버튼
            productUtil.getProductById(productId) { STATUS_CODE, product ->
                if (STATUS_CODE == StatusCode.SUCCESS) {
                    chatUtil.getUserChatRooms(userUtil.whoAmI()?.uid ?: "") { chatRoomsStatus, chatRooms ->
                        if (chatRoomsStatus == StatusCode.SUCCESS) {
                            val existingChatRoom = chatRooms?.find { chatRoom ->
                                chatRoom.productId == productId && (chatRoom.buyerId == userUtil.whoAmI()?.uid ?: "" || chatRoom.sellerId == product?.sellerId ?: "")
                            }

                            if (existingChatRoom != null) {
                                // 존재하는 채팅방 불러오기
                                val intent = Intent(this, ChatActivity::class.java)
                                intent.putExtra("chatRoomId", existingChatRoom.chatRoomId)
                                startActivity(intent)
                            } else {
                                // 채팅방 생성
                                chatUtil.createChatRoom(productId = productId ?: "", buyerId = userUtil.whoAmI()?.uid ?: "", sellerId = product?.sellerId ?: "") { newChatRoomStatus, newChatRoomId ->
                                    if (newChatRoomStatus == StatusCode.SUCCESS) {
                                        val intent = Intent(this, ChatActivity::class.java)
                                        intent.putExtra("chatRoomId", newChatRoomId)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "채팅방 생성 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "채팅방 목록을 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "채팅방 생성 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    startActivity(Intent(this, ChatlistActivity::class.java))
                    true
                }
                R.id.mypage -> {
                    startActivity(Intent(this, MypageActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getProductStatusString(stateCode: Int): String {
        return when (stateCode) {
            0 -> "판매중"
            1 -> "예약중"
            2 -> "판매 완료"
            else -> "알 수 없는 상태"
        }
    }


}
