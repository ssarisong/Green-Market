package kr.ac.hansung.greenmarket.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.hansung.greenmarket.R

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // 클릭한 제품의 정보 받아오기 (이 예시에서는 이름만 받아옴)
        val productName = intent.getStringExtra("productName")

        // TODO: productName을 사용하여 제품의 세부 정보를 표시하는 코드 작성
    }
}
