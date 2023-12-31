package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.Timestamp
import kr.ac.hansung.greenmarket.ProductStates
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.FirestoreProductModel
import kr.ac.hansung.greenmarket.models.Product

/**
 * Firebase 중고거래 상품 관련 작업을 처리하는 유틸리티 클래스입니다.
 * 상품 추가, 조회 기능을 제공합니다.
 */
class FirebaseProductUtil {

    private val productModel = FirestoreProductModel()

    /**
     * 중고거래 상품을 새로 등록합니다.
     *
     * @param userId 상품을 등록하는 사용자의 ID입니다.
     * @param productNm 등록할 상품의 이름입니다.
     * @param productImg 등록할 상품의 이미지입니다.
     * @param productDetail 등록할 상품의 상세설명입니다.
     * @param productPrice 등록할 상품의 가격입니다.
     * @param callback 상품 등록 성공 시 상태 코드(STATUS_CODE)와 상품의 ID를 인자로 받는 콜백 함수입니다.
     */
    fun createProduct(userId: String, productNm: String, productImg: String, productDetail: String, productPrice: Int, callback: (Int, String?) -> Unit) {
        val product = Product("", userId, productNm, productImg, productDetail, productPrice, ProductStates.ON_SALE.code, Timestamp.now())

        productModel.insertProduct(product) { STATUS_CODE, pid ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS, pid)
            }else{
                callback(StatusCode.FAILURE, null)
            }
        }
    }

    /**
     * ID 사용하여 상품 객체들을 검색합니다.
     *
     * @param productId 검색할 상품의 ID입니다.
     * @param callback 상품 정보 조회 성공 시 상태 코드(STATUS_CODE)와 상품 객체를 인자로 받는 콜백 함수입니다.
     */
    fun getProductById(productId: String, callback: (Int, Product?) -> Unit){
        productModel.getProductsById(productId){ STATUS_CODE, product ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                if(product != null){
                    callback(StatusCode.SUCCESS, product)
                }else{
                    Log.e("FirebaseProductUtils", "[${productId}] ID를 가진 상품이 없음")
                    callback(StatusCode.FAILURE, null)
                }
            }else{
                callback(StatusCode.FAILURE, null)
            }
        }
    }

    /**
     * 모든 제품의 목록을 실시간으로 조회합니다.
     *
     * @param callback 전체 상품 정보 조회 성공 시 상태 코드(STATUS_CODE)와 상품 객체 리스트를 인자로 받는 콜백 함수입니다.
     */
    fun getAllProducts(callback: (Int, List<Product>?) -> Unit) {
        productModel.getProducts(){ STATUS_CODE, productList ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS, productList)
            } else{
                callback(StatusCode.FAILURE, null)
            }
        }
    }

    /**
     * 중고상품 ID를 받아 해당 상품을 삭제합니다.
     *
     * @param productId 삭제할 상품의 ID입니다.
     * @param callback 상품 삭제 성공 여부를 알려주는 상태 코드(STATUS_CODE)를 인자로 받는 콜백 함수입니다.
     */
    fun deleteProduct(productId: String, callback: (Int) -> Unit) {
        productModel.deleteproduct(productId){ STATUS_CODE ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS)
            } else{
                callback(StatusCode.FAILURE)
            }
        }
    }

    /**
     * 판매자의 ID를 사용하여 해당 판매자의 모든 판매 상품 목록을 가져옵니다.
     *
     * @param callback 상품 조회 성공 시 상태 코드(STATUS_CODE)와 해당 사용자가 등록한 상품 리스트를 인자로 받는 콜백 함수입니다.
     */
    fun getProductsBySellerId(sellerId: String, callback: (Int, List<Product>?) -> Unit) {
        productModel.getProducts() {STATUS_CODE, products ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS, products?.filter { it.sellerId == sellerId })
            } else{
                callback(StatusCode.FAILURE, null)
            }
        }
    }

    /**
     * 중고상품의 정보를 수정합니다.
     *
     * @param productId 상품의 고유 ID입니다.
     * @param updatedTitle 수정된 상품의 이름입니다.
     * @param updateImage 수정된 상품의 이미지 url입니다.
     * @param updatedDetail 수정된 상품의 상세 정보입니다.
     * @param updatedPrice 수정된 상품의 가격입니다.
     * @param updatedStateCode 수정된 상품의 상태 코드입니다.
     * @param callback 상품 정보 수정 상태 코드(STATUS_CODE)를 반환하는 콜백 함수입니다.
     */
    fun updateProduct(productId: String, updatedTitle: String, updateImage: String?, updatedDetail: String, updatedPrice: Int, updatedStateCode: Int, callback: (Int) -> Unit) {
        productModel.updateProduct(productId, updatedTitle, updateImage, updatedDetail, updatedPrice, updatedStateCode) { STATUS_CODE ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS)
            } else {
                callback(StatusCode.FAILURE)
            }
        }
    }
}