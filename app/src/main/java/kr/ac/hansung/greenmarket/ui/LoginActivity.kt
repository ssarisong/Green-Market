package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userUtil = FirebaseUserUtil()

        findViewById<Button>(R.id.button_login).setOnClickListener {
            val userEmail = findViewById<EditText>(R.id.editText_id)?.text.toString()
            val password = findViewById<EditText>(R.id.editText_pw)?.text.toString()
            userUtil.doSignIn(userEmail, password) { STATUS_CODE, uid ->
                if (STATUS_CODE == StatusCode.SUCCESS) {
                    // 로그인 성공
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
                    // 로그인 실패
                }
            }
        }
        findViewById<Button>(R.id.button_join).setOnClickListener {
            // button_join 버튼이 클릭되면 activity_join으로 화면 전환
            startActivity(
                Intent(this, JoinActivity::class.java)
            )
        }
    }
}
