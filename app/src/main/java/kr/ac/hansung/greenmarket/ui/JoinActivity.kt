package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val userUtil = FirebaseUserUtil()

        // 버튼 클릭 시 DatePickerActivity로 이동
        findViewById<Button>(R.id.button_DatePicker).setOnClickListener {
            startActivity(Intent(this, DatePickerActivity::class.java))
        }

        findViewById<Button>(R.id.button_Join).setOnClickListener { // 회원가입 버튼
            val userEmail = findViewById<EditText>(R.id.editText_Email)?.text.toString()
            val password = findViewById<EditText>(R.id.editText_Pw)?.text.toString()
            val name = findViewById<EditText>(R.id.editText_Name)?.text.toString()
            val birth = findViewById<Button>(R.id.button_DatePicker)?.text.toString()

            userUtil.doSignUp(userEmail, password, name, birth) { STATUS_CODE, uid ->
                if (STATUS_CODE == StatusCode.SUCCESS) {
                    // 회원가입 성공
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                    finish()
                } else {
                    // 회원가입 실패
                }
            }
        }

        findViewById<Button>(R.id.button_Back).setOnClickListener { // 이전 버튼
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
    }
}
