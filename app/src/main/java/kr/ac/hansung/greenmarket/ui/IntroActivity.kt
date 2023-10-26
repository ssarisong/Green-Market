package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kr.ac.hansung.greenmarket.R

class IntroActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_layout) // xml, Java 소스 연결
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent) // 다음 화면으로 넘어갑니다
            finish()
        }, 3000) // 3초 뒤에 Runnable 객체를 실행하도록 함
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
