package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class MypostListActivity : AppCompatActivity() {
    private val firebaseProductUtil = FirebaseProductUtil()
    private val firebaseUserUtil = FirebaseUserUtil()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypostlist)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_mypostlist)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(this, emptyList())
        recyclerView.adapter = productAdapter

        // 현재 사용자의 ID를 가져옵니다.
        val userId = firebaseUserUtil.whoAmI()?.uid ?: ""

        // 판매자 ID로 상품을 가져오는 함수를 호출합니다.
        firebaseProductUtil.getProductsBySellerId(userId) { STATUS_CODE, productList ->
            if (STATUS_CODE == StatusCode.SUCCESS) {
                // RecyclerView 어댑터를 필터링된 productList로 설정합니다.
                productAdapter = ProductAdapter(this, productList ?: emptyList())
                recyclerView.adapter = productAdapter
            } else { // 실패 시 처리
                Log.e("MypostListActivity", "상품을 불러오는 중 오류가 발생했습니다.")
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomNav.selectedItemId = R.id.mypage

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

    }
}
