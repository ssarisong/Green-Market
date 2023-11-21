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

        productUtil.getAllProducts() {STATUS_CODE, productList ->
            if(STATUS_CODE==StatusCode.SUCCESS){
                recyclerView.adapter = ProductAdapter(this, productList?: emptyList<Product>())
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
            // 검색 버튼을 클릭한 경우, 검색어를 이용하여 데이터를 필터링하거나 처리할 로직을 추가할 수 있습니다.
            val searchQuery = searchEditText.text.toString().trim()
            if (searchQuery.isNotEmpty()) {
//                Toast.makeText(this, "검색어: $searchQuery", Toast.LENGTH_SHORT).show() // 확인용..
            }
        }
    }

    private fun showFilterDialog() {
        val items = arrayOf(
            "판매중", "예약중", "판매 완료"
        )

        val checkedItems = BooleanArray(items.size)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("검색 필터")
            .setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("확인") { dialog, _ ->
                val selectedFilters = mutableListOf<String>()
                for (i in items.indices) {
                    if (checkedItems[i]) {
                        selectedFilters.add(items[i])
                    }
                }
                Toast.makeText(
                    this,
                    "${selectedFilters.joinToString(", ")} 상품만 표시됩니다.",
                    Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }


}