package kr.ac.hansung.greenmarket.utils

import android.util.Log
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
     * @param productNm 등록할 상품의 이름입니다.
     * @param productImg 등록할 상품의 이미지입니다.
     * @param productDetail 등록할 상품의 상세설명입니다.
     * @param productPrice 등록할 상품의 가격입니다.
     * @param callback 상품 등록 성공 시 상태 코드(STATUS_CODE)와 상품의 ID를 인자로 받는 콜백 함수입니다.
     */
    fun createProduct(productNm: String, productImg: String, productDetail: String, productPrice: Int, callback: (Int, String) -> Unit) {
        val product = Product(productNm, productImg, productDetail, productPrice, 0)

        productModel.insertProduct(product) { STATUS_CODE, pid ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS, pid)
            }else{
                callback(StatusCode.FAILURE, pid)
            }
        }
    }

    /**
     * ID 사용하여 상품 객체들을 검색합니다.
     *
     * @param productId 검색할 상품의 ID입니다.
     * @param callback 상품 정보 조회 성공 시 상태 코드(STATUS_CODE)와 상품 객체를 인자로 받는 콜백 함수입니다.
     */
    fun SearchById(productId: String, callback: (Int, Product?) -> Unit){
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

    fun getAllProducts(callback: (Int, List<Product>?) -> Unit) {
        productModel.getProducts(){ STATUS_CODE, productList ->
            if(STATUS_CODE == StatusCode.SUCCESS){
                callback(StatusCode.SUCCESS, productList)
            } else{
                callback(StatusCode.FAILURE, null)
            }
        }
    }
}