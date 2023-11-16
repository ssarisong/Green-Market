package kr.ac.hansung.greenmarket.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_menu)

        // 초기 화면을 "홈" 메뉴로 설정
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // "홈" 메뉴를 클릭한 경우, 홈 화면으로 이동
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.chatting -> {
                    // "채팅" 메뉴를 클릭한 경우, 채팅 화면으로 이동
                    startActivity(Intent(this, ChatlistActivity::class.java))
                    true
                }
                R.id.mypage -> {
                    // "마이페이지" 메뉴를 클릭한 경우, 마이페이지 화면으로 이동
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
