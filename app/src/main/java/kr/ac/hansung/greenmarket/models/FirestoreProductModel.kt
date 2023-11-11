package kr.ac.hansung.greenmarket.models

import android.net.http.UrlRequest.Status
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import kr.ac.hansung.greenmarket.StatusCode

/**
 * Firestore를 사용하여 상품 데이터를 관리하는 모델 클래스입니다.
 */

class FirestoreProductModel {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Firestore에 상품을 추가합니다.
     *
     * @param pid 상품의 고유 ID입니다.
     * @param product 상품 정보를 담고 있는 Product 객체입니다.
     * @param callback 상태 코드(STATUS_CODE)를 반환하는 콜백 함수입니다.
     */

    fun insertProduct(pid: String, product: Product, callback: (Int)->Unit){
        db.collection("Product").document(pid).set(product)
            .addOnSuccessListener {
                Log.d("FirestoreProductModel", "${product.productDetail}(${product.productImg})(${product.productNm})(${product.productPrice}) 상품 DB 성공적으로 생성 -> ID: [${pid}]")
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener{ e->
                Log.w("FirestoreProductModel", "상품 DB 정보 생성 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE)
            }
    }

    /**
     * Firestore에서 특정 상품을 삭제합니다.
     *
     * @param pid 상세 정보를 조회할 사용자의 고유 ID입니다.
     * @param callback 사용자 정보 조회 상태 코드(STATUS_CODE)와 Product 객체를 반환하는 콜백 함수입니다.
     */

    fun deleteproduct(pid: String, callback: (Int) -> Unit) {
        db.collection("Product").document(pid).delete()
            .addOnSuccessListener {
                Log.d("FirestoreProductModel", "ID: [${pid}] 상품 DB 정보 성공적으로 삭제 ->")
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreProductModel", "ID: [${pid}] 상품 DB 정보 삭제 중 에러 발생!!! ->", e)
                callback(StatusCode.FAILURE)
            }
    }

    /**
     * Firestore에서 특정 상품의 상세 정보를 조회합니다.
     *
     * @param productNm 상세 정보를 조회할 상품의 이름입니다.
     * @param callback 사용자 정보 조회 상태 코드(STATUS_CODE)와 User 객체를 반환하는 콜백 함수입니다.
     */

    fun getProductModel(productNm: String, callback:(Int, Product?) -> Unit){
        db.collection("Product").document(productNm).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()){
                    Log.d("FirestoreProductModel", "ID: [${productNm}] 상품 DB에 블러오기 성공")
                    val product = documentSnapshot.toObject(Product::class.java)
                    callback(StatusCode.SUCCESS, product)
                }else{
                    Log.d("FirestoreProductModel", "ID: [${productNm}] 상품이 DB에 존재하지 않음")
                    callback(StatusCode.FAILURE, null)
                }
            }
            .addOnFailureListener{e ->
                Log.w("FirestoreProductModel", "ID: [${productNm}] 상품을 DB에서 불러오는 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE, null)
            }
    }
}