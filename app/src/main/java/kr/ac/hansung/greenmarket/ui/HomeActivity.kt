package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.Product
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil

class HomeActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val productUtil = FirebaseProductUtil()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchEditText = findViewById(R.id.search_edit_text)

        productUtil.getAllProducts { STATUS_CODE, productList ->
            if (STATUS_CODE == StatusCode.SUCCESS) {
                val filteredList = productList?.filter { product ->
                    isFilterChecked("판매중", product.stateCode) ||
                            isFilterChecked("예약중", product.stateCode) ||
                            isFilterChecked("판매 완료", product.stateCode)
                }
                recyclerView.adapter = ProductAdapter(this, filteredList ?: emptyList())
            }
        }


        val layoutManager = GridLayoutManager(this, 2) // 2는 한 줄에 표시되는 아이템 수
        recyclerView.layoutManager = layoutManager


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)

        // 초기 화면을 "홈" 메뉴로 설정
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

        val writeButton = findViewById<Button>(R.id.write_button)

        writeButton.setOnClickListener {
            // "글쓰기" 버튼을 클릭한 경우, NewPostActivity로 이동
            startActivity(Intent(this, NewPostActivity::class.java))
        }

        val filterButton = findViewById<ImageButton>(R.id.filter)

        filterButton.setOnClickListener {
            showFilterDialog()
        }

        val searchButton = findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString().trim()

            if (searchQuery.isNotEmpty()) {
                val productUtil = FirebaseProductUtil()
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                productUtil.getAllProducts { STATUS_CODE, productList ->
                    if (STATUS_CODE == StatusCode.SUCCESS) {
                        // 제품명 또는 상세정보가 검색어와 일치하는 상품만 필터링
                        val filteredList = productList?.filter { product ->
                            product.name.contains(searchQuery, ignoreCase = true) ||
                                    product.detail.contains(searchQuery, ignoreCase = true)
                        }
                        recyclerView.adapter = ProductAdapter(this, filteredList ?: emptyList())
                    }
                }
            } else {
                // 검색어가 비어있으면 모든 상품을 표시
                val productUtil = FirebaseProductUtil()
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                productUtil.getAllProducts { STATUS_CODE, productList ->
                    if (STATUS_CODE == StatusCode.SUCCESS) {
                        val filteredList = productList ?: emptyList()
                        recyclerView.adapter = ProductAdapter(this, filteredList)
                    }
                }
            }
        }
    }

    private var selectedFilters = booleanArrayOf(true, true, true)
    private fun showFilterDialog() {
        val items = arrayOf("판매중", "예약중", "판매 완료")
        val checkedItems = selectedFilters.copyOf()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("검색 필터")
            .setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("확인") { dialog, _ ->
                selectedFilters = checkedItems.copyOf()

                val productUtil = FirebaseProductUtil()
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

                productUtil.getAllProducts { STATUS_CODE, productList ->
                    if (STATUS_CODE == StatusCode.SUCCESS) {
                        val filteredList = productList?.filter { product ->
                            isFilterChecked("판매중", product.stateCode) && selectedFilters[0] ||
                                    isFilterChecked("예약중", product.stateCode) && selectedFilters[1] ||
                                    isFilterChecked("판매 완료", product.stateCode) && selectedFilters[2]
                        }
                        recyclerView.adapter = ProductAdapter(this, filteredList ?: emptyList())
                    }
                }

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun isFilterChecked(filter: String, statusCode: Int): Boolean {
        return when (filter) {
            "판매중" -> statusCode == 0
            "예약중" -> statusCode == 1
            "판매 완료" -> statusCode == 2
            else -> false
        }
    }

}