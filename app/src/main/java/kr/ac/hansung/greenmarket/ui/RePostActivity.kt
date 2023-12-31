package kr.ac.hansung.greenmarket.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.hansung.greenmarket.ProductStates
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.Product
import kr.ac.hansung.greenmarket.utils.FirebaseProductUtil

class RePostActivity : AppCompatActivity() {
    private lateinit var product: Product
    private lateinit var tempState: ProductStates
    private var productUtil: FirebaseProductUtil = FirebaseProductUtil()
    private var selectedImageUri: Uri? = null
    private var selectedImageString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_post)

        val productId = intent.getStringExtra("productId")
        if (productId != null) {
            val productUtil = FirebaseProductUtil()
            productUtil.getProductById(productId) { statusCode, retrievedProduct ->
                if (statusCode == StatusCode.SUCCESS && retrievedProduct != null) {
                    product = retrievedProduct
                    selectedImageUri = Uri.parse(product.img)
                    tempState = ProductStates.values().first { it.code == product.stateCode }
                    updateUIWithProductInfo()
                } else {
                    // 실패할 경우 처리
                }
            }
        }

        findViewById<Button>(R.id.img_upload).setOnClickListener {
            openImageChooser()
        }

        // 수정 완료 버튼 클릭 시
        findViewById<TextView>(R.id.btnUpdate).setOnClickListener {
            // 수정된 정보 가져오기
            val updatedTitle = findViewById<EditText>(R.id.editTitle).text.toString()
            val updatedDetail = findViewById<EditText>(R.id.editProductDetail).text.toString()
            val updatedPrice = findViewById<EditText>(R.id.editPrice).text.toString()
            val updatedStateCode = tempState.code

            if (updatedTitle.isNotEmpty() && updatedDetail.isNotEmpty() && updatedPrice.isNotEmpty() && selectedImageUri != null && updatedStateCode != null) {
                try {
                    val priceValue = updatedPrice.toInt()
                    if (priceValue >= 0 && priceValue <= Int.MAX_VALUE) {
                        productUtil.updateProduct(productId = product.productId, updatedTitle = updatedTitle, updateImage = selectedImageString, updatedDetail = updatedDetail, updatedPrice = updatedPrice.toInt(), updatedStateCode = updatedStateCode) { STATUS_CODE ->
                            if (STATUS_CODE == StatusCode.SUCCESS) {
                                Toast.makeText(this, "상품이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                navigateToMyPostList()
                            } else {
                                Toast.makeText(this, "알수없는 이유로 상품 수정이 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                navigateToMyPostList()
                            }
                        }
                    } else {
                        // 가격 범위가 유효하지 않음
                        Toast.makeText(this, "가격이 유효한 범위에 있지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "유효한 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 필드 중 하나 이상이 비어 있음
                Toast.makeText(this, "모든 필수값을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 글 삭제 버튼 클릭 시
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val productUtil = FirebaseProductUtil()
            productUtil.deleteProduct(product.productId) { statusCode ->
                if (statusCode == StatusCode.SUCCESS) {
                    Toast.makeText(this, "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    // 돌아갈 화면으로 이동
                    navigateToMyPostList()
                } else {
                    Toast.makeText(this, "알수없는 이유로 상품 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 상태 변경 버튼 클릭 시
        findViewById<Button>(R.id.btnChangeStatus).setOnClickListener {
            showStatusDialog()
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
    private fun updateUIWithProductInfo() {
        // UI 업데이트
        findViewById<EditText>(R.id.editTitle).setText(product.name)
        findViewById<EditText>(R.id.editProductDetail).setText(product.detail)
        findViewById<EditText>(R.id.editPrice).setText(product.price.toString())

        // 이미지 미리보기 업데이트
        val imgMain2 = findViewById<ImageView>(R.id.img_main2)
        Glide.with(this)
            .load(product.img)
            .into(imgMain2)

        // 상태 텍스트뷰 및 버튼 업데이트
        findViewById<TextView>(R.id.statusTextView).text = ProductStates.values().first { it.code == product.stateCode }.description
    }

    // 다이얼로그 표시 함수
    private fun showStatusDialog() {
        val statusOptions = ProductStates.values().map { it.description }.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("상태 변경")
            .setItems(statusOptions) { _, which ->
                tempState = ProductStates.values()[which]
                findViewById<TextView>(R.id.statusTextView).text = tempState.description
            }

        builder.create().show()
    }

    // 화면을 MypostListActivity로 이동하는 함수
    private fun navigateToMyPostList() {
        val intent = Intent(this, MypostListActivity::class.java)
        startActivity(intent)
        finish() // 현재 화면 종료
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RePostActivity.IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RePostActivity.IMAGE_PICK_CODE) {
            selectedImageUri = data?.data
            selectedImageString = selectedImageUri.toString()
            Glide.with(this)
                .load(selectedImageUri)
                .into(findViewById(R.id.img_main2))
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
