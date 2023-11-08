package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R

class MypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    startActivity(Intent(this, ChattingActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val btnName = findViewById<ImageButton>(R.id.btn_name)
        btnName.setOnClickListener {
            // 사용자 이름 버튼 클릭 시 실행할 코드
        }

        val btnMypost = findViewById<ImageButton>(R.id.btn_mypost)
        btnMypost.setOnClickListener {
            // 내가 쓴 글 버튼 클릭 시 실행할 코드
        }

        val btnMyproduct = findViewById<ImageButton>(R.id.btn_myproduct)
        btnMyproduct.setOnClickListener {
            // 내가 구매한 상품 버튼 클릭 시 실행할 코드
        }

        val btnLogout = findViewById<ImageButton>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            // 로그아웃 버튼 클릭 시 LoginActivity로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // 로그아웃
        }

    }
}
