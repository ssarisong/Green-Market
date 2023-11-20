package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productUtil = FirebaseProductUtil()

        // 클릭한 제품의 정보 받아오기
        val productId = intent.getStringExtra("productId")

        if (productId != null) {
            productUtil.SearchById(productId) { STATUS_CODE, product ->
                if(STATUS_CODE==StatusCode.SUCCESS){
                    findViewById<TextView>(R.id.tv_title2).text = product?.name ?: "로딩 실패"
                    findViewById<TextView>(R.id.tv_product_detail2).text = product?.detail ?: "로딩 실패"
                    findViewById<TextView>(R.id.tv_price2).text = product?.price.toString()
                    Glide.with(this)
                        .load(product?.img)
                        .into(findViewById<ImageView>(R.id.img_main))
                }
            }
        }
        val quitButton: ImageButton = findViewById(R.id.btn_quit)
        val chatButton: Button = findViewById(R.id.btn_chatting)

        quitButton.setOnClickListener {// 이전 버튼
            startActivity(Intent(this, HomeActivity::class.java))
        }
        chatButton.setOnClickListener {// 채팅하기 버튼
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }
}
