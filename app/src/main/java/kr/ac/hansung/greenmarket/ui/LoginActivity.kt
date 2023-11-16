package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

            val passwordEditText = findViewById<EditText>(R.id.editText_pw)
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            if (userEmail.isEmpty()) {
                findViewById<TextView>(R.id.textView_login_failed).text = "이메일을 입력하세요"
                findViewById<TextView>(R.id.textView_login_failed).visibility = View.VISIBLE
            } else if (password.isEmpty()) {
                findViewById<TextView>(R.id.textView_login_failed).text = "비밀번호를 입력하세요"
                findViewById<TextView>(R.id.textView_login_failed).visibility = View.VISIBLE
            } else {
                // 이메일과 비밀번호가 입력되었을 때 로그인 시도
                userUtil.doSignIn(userEmail, password) { STATUS_CODE, uid ->
                    if (STATUS_CODE == StatusCode.SUCCESS) {
                        // 로그인 성공
                        startActivity(
                            Intent(this, HomeActivity::class.java)
                        )
                        finish()
                    } else {
                        // 로그인 실패
                        findViewById<TextView>(R.id.textView_login_failed).text = "로그인에 실패했습니다"
                    }
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