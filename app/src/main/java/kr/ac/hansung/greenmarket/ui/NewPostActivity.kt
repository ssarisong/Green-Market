package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)

        val userUtil = FirebaseUserUtil()
        val productUtil = FirebaseProductUtil()

        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    startActivity(Intent(this, ChatlistActivity::class.java))
                    true
                }
                R.id.mypage -> {
                    startActivity(Intent(this, MypageActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val btnFinish = findViewById<Button>(R.id.btn_finish)

        btnFinish.setOnClickListener {
            val title = findViewById<EditText>(R.id.tv_title).text.toString()
            val price = findViewById<EditText>(R.id.tv_price).text.toString()
            val detail = findViewById<EditText>(R.id.et_detail).text.toString()

            val intentProductDetail = Intent(this, ProductDetailActivity::class.java)

            val intentHome = Intent(this, HomeActivity::class.java)
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the back stack

            productUtil.createProduct(userUtil.whoAmI()?.uid?: "", title, "img", detail, price.toInt()) { STATUS_CODE, newProductId ->
                if(STATUS_CODE == StatusCode.SUCCESS){
                    // 상품 등록이 성공한 경우
                    intentProductDetail.putExtra("productId", newProductId)
                    startActivity(intentProductDetail)
                } else{
                    // 상품 등록이 실패한 경우
                    startActivity(intentHome)
                }
            }
        }
    }
}
