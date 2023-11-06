package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNav.inflateMenu(R.menu.bottom_menu)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chatting -> {
                    startActivity(Intent(this, ChattingActivity::class.java))
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
}