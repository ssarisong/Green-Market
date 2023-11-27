package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil

class NewPostActivity : AppCompatActivity() {
    private var selectedImageUri: Uri? = null
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

        val btnAddPhoto = findViewById<Button>(R.id.btn_addphoto)
        btnAddPhoto.setOnClickListener {
            openImageChooser()
        }

        val btnFinish = findViewById<Button>(R.id.btn_finish)

        btnFinish.setOnClickListener {
            val title = findViewById<EditText>(R.id.tv_title).text.toString()
            val price = findViewById<EditText>(R.id.tv_price).text.toString()
            val detail = findViewById<EditText>(R.id.et_detail).text.toString()

            if (title.isNotEmpty() && detail.isNotEmpty() && price.isNotEmpty() && selectedImageUri != null) {
                try {
                    val priceValue = price.toInt()
                    if (priceValue >= 0 && priceValue <= Int.MAX_VALUE) {
                        productUtil.createProduct(userUtil.whoAmI()?.uid?: "", title, selectedImageUri.toString(), detail, price.toInt()) { STATUS_CODE, newProductId ->
                            if(STATUS_CODE == StatusCode.SUCCESS){
                                // 상품 등록이 성공한 경우
                                Toast.makeText(this, "상품 등록이 성공했습니다.", Toast.LENGTH_SHORT).show()
                                val intentProductDetail = Intent(this, ProductDetailActivity::class.java)
                                intentProductDetail.putExtra("productId", newProductId)
                                startActivity(intentProductDetail)
                            } else{
                                // 상품 등록이 실패한 경우
                                Toast.makeText(this, "알수없는 이유로 상품등록이 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // 가격 범위가 유효하지 않음
                        Toast.makeText(this, "가격이 유효한 범위에 있지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    // 가격이 정수가 아님
                    Toast.makeText(this, "유효한 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 필드 중 하나 이상이 비어 있음
                Toast.makeText(this, "모든 필수값을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data
            Glide.with(this)
                .load(selectedImageUri)
                .into(findViewById(R.id.img_main))
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
