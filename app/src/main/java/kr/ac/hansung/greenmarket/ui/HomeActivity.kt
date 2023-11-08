package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)

        // 초기 화면을 "홈" 메뉴로 설정
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // "홈" 메뉴를 클릭한 경우, 홈 화면으로 이동
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    // "채팅" 메뉴를 클릭한 경우, 채팅 화면으로 이동
                    startActivity(Intent(this, ChattingActivity::class.java))
                    true
                }
                R.id.mypage -> {
                    // "마이페이지" 메뉴를 클릭한 경우, 마이페이지 화면으로 이동
                    startActivity(Intent(this, MypageActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
