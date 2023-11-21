package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class MypageActivity : AppCompatActivity() {
    private lateinit var userUtil: FirebaseUserUtil
    private lateinit var textName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        userUtil = FirebaseUserUtil()

        textName = findViewById(R.id.text_name)

        // TextView에 사용자 이름 업데이트하기
        updateUserName()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNav.selectedItemId = R.id.mypage

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    startActivity(Intent(this, ChatlistActivity::class.java))
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
            val intent = Intent(this, MypostListActivity::class.java)
            startActivity(intent)
        }

        val btnLogout = findViewById<ImageButton>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            userUtil.doSignOut { STATUS_CODE ->
                if(STATUS_CODE==StatusCode.SUCCESS){
                    // 로그아웃 버튼 클릭 시 LoginActivity로 이동
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()  // 로그아웃
                }
            }
        }

    }

    private fun updateUserName() {
        val currentUser = userUtil.whoAmI()

        if (currentUser != null) {
            // UID를 기반으로 Firebase에서 사용자 정보 가져오기
            userUtil.getUser(currentUser.uid) { statusCode, user ->
                if (statusCode == StatusCode.SUCCESS && user != null) {
                    // text_name TextView에 사용자 이름 업데이트
                    textName.text = user.name
                }
            }
        }
    }

}
