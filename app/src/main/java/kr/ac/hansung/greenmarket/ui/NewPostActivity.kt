package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.models.Product

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
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

            val product = Product(name = title, detail = detail, price = price.toInt())

            val intentProductDetail = Intent(this, ProductDetailActivity::class.java)
            intentProductDetail.putExtra("productName", product.name)

            val intentHome = Intent(this, HomeActivity::class.java)
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the back stack

            // Start both activities
            startActivity(intentProductDetail)
            startActivity(intentHome)
        }
    }
}
