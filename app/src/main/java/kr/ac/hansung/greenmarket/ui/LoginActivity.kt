package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.utils.FirebaseLoginUtils


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.button_login).setOnClickListener{
            val userEmail = findViewById<EditText>(R.id.editText_id)?.text.toString()
            val password = findViewById<EditText>(R.id.editText_pw)?.text.toString()
            val loginUtil = FirebaseLoginUtils()
            loginUtil.doLogin(userEmail, password) { uid ->
                if (uid != null) {
                    // 로그인 성공
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // 로그인 실패
                }
            }
        }
    }
}
