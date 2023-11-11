package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.FirestoreProductModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class FirebaseProductUtil {
    private val productModel = FirestoreProductModel()

    /**
     * 이메일과 비밀번호를 사용하여 사용자 로그인을 수행합니다.
     *
     * @param productNm 등록할 상품의 이름입니다.
     * @param productPrice 등록할 상품의 가격입니다.
     * @param productDetail 등록할 상품의 상세설명입니다.
     * @param productImg 등록할 상품의 이미지입니다.
     * @param callback 상품 등록 성공 시 상태 코드(STATUS_CODE)와 사용자 ID를 인자로 받는 콜백 함수입니다.
     */

    fun doAddPd(productNm: String, productPrice: String, productDetail: String, productImg: String, callback: (Int, String) -> Unit) {
        val pid = generateRM().toString() // You need to implement a function to generate a unique product ID

        val product = Product(productNm, productPrice, productDetail, productImg)

        // Assuming your FirestoreProductModel instance is already created as `productModel`
        productModel.insertProduct(pid, product) { STATUS_CODE ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                Log.d("FirebaseProductUtils", "Firestore에서 상품 성공적으로 생성 -> ID: [${pid}]")
                callback(StatusCode.SUCCESS, pid)
            }else{
                Log.e("FirebaseProductUtils", "Firestore에서 상품 생성 실패 -> ID: [${pid}]")
                callback(StatusCode.FAILURE, pid)
            }
        }
    }

    /**
     * 랜덤한 번호를 생성하여 상품마다 랜덤한 아이디를 부여해줍니다
     */
    fun generateRM(): Long {
        val rM = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
        return rM
    }

    /**
     * productNm 사용하여 상품 객체들을 검색합니다.
     *
     * @param productNm 검색할 상품의 이름입니다.
     * @param callback 상품 정보 조회 성공 시 상태 코드(STATUS_CODE)와 상품 객체를 인자로 받는 콜백 함수입니다.
     */

    fun getProduct(productNm: String, callback: (Int, List<Product>?) -> Unit){
        val productList = mutableListOf<Product>()

        productModel.getProductModel(productNm){ STATUS_CODE, product ->
            when(STATUS_CODE){
                StatusCode.SUCCESS -> {
                    if(product != null){
                        productList.add(product)
                        callback(StatusCode.SUCCESS, productList)
                    }else{
                        Log.e("FirebaseProductUtils", "productNm == null")
                        callback(StatusCode.FAILURE, null)
                    }
                }else->{
                    Log.e("FirebaseProductUtils", "상품 이름으로 검색 중 에러 발생!!!")
                }
            }
        }
    }


}