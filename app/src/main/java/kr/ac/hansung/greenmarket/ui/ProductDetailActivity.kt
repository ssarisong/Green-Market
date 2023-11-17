package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kr.ac.hansung.greenmarket.R

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // 클릭한 제품의 정보 받아오기 (이 예시에서는 이름만 받아옴)
        val productName = intent.getStringExtra("productName")

        val quitButton: ImageButton = findViewById(R.id.btn_quit)
        val chatButton: Button = findViewById(R.id.btn_chatting)

        quitButton.setOnClickListener {// 이전 버튼
            startActivity(Intent(this, HomeActivity::class.java))
        }
        chatButton.setOnClickListener {// 채팅하기 버튼
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }
}
