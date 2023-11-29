package kr.ac.hansung.greenmarket.models

import android.net.Uri
import android.net.http.UrlRequest.Status
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kr.ac.hansung.greenmarket.StatusCode
import java.util.UUID

/**
 * Firestore를 사용하여 상품 데이터를 관리하는 모델 클래스입니다.
 */
class FirestoreProductModel {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Firestore에 상품을 추가합니다.
     *
     * @param product 상품 정보를 담고 있는 Product 객체입니다.
     * @param callback 상태 코드(STATUS_CODE)와 추가된 상품의 ID를 반환하는 콜백 함수입니다.
     */
    fun insertProduct(product: Product, callback: (Int, String?)->Unit) {
        val newProduct = db.collection("Product").document()
        val storageRef = FirebaseStorage.getInstance().reference
        val imgFilePath = "Product/${newProduct.id}"
        val imageRef = storageRef.child(imgFilePath)

        imageRef.putFile(Uri.parse(product.img))
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val updatedProduct =
                        product.copy(productId = newProduct.id, img = uri.toString())

                    newProduct.set(updatedProduct).addOnSuccessListener {
                        Log.d(
                            "FirestoreProductModel",
                            "([${newProduct.id}]:상품명[${product.name}] 상품 DB 성공적으로 생성"
                        )
                        callback(StatusCode.SUCCESS, newProduct.id)
                    }.addOnFailureListener { e ->
                        Log.w("FirestoreProductModel", "[${newProduct.id}]:상품명[${product.name}] 상품 DB 정보 생성 중 에러 발생!!! -> ", e)
                        callback(StatusCode.FAILURE, null)
                    }
                }.addOnFailureListener { e ->
                    Log.w("FirestoreProductModel", "[${newProduct.id}]:상품명[${product.name}] 상품 이미지 다운로드 URL 받기 실패!!! -> ", e)
                    callback(StatusCode.FAILURE, null)
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreProductModel", "[${newProduct.id}]:상품명[${product.name}]상품 이미지 저장 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE, null)
            }
    }

    /**
     * Firestore에서 특정 상품을 삭제합니다.
     *
     * @param pid 상품의 고유 ID입니다.
     * @param callback 상품 삭제 결과에 대한 상태 코드(STATUS_CODE)를 반환하는 콜백 함수입니다.
     */
    fun deleteproduct(pid: String, callback: (Int) -> Unit) {
        val imageRef = FirebaseStorage.getInstance().reference.child("Product/${pid}")
        imageRef.metadata
            .addOnSuccessListener {
                imageRef.delete()
                    .addOnSuccessListener {
                        db.collection("Product").document(pid).delete()
                            .addOnSuccessListener {
                                Log.d("FirestoreProductModel", "[${pid}] 상품 DB 정보 성공적으로 삭제")
                                callback(StatusCode.SUCCESS)
                            }
                            .addOnFailureListener { e ->
                                Log.w("FirestoreProductModel", "[${pid}] 상품 DB 정보 삭제 중 에러 발생!!! -> ", e)
                                callback(StatusCode.FAILURE)
                            }
                    } .addOnFailureListener { e ->
                        Log.w("FirestoreProductModel", "[${pid}] 상품 이미지 Storage에서 삭제 중 에러 발생!!! -> ", e)
                        callback(StatusCode.FAILURE)
                    }
            }
            .addOnFailureListener {
                db.collection("Product").document(pid).delete()
                    .addOnSuccessListener {
                        Log.d("FirestoreProductModel", "[${pid}] 상품 DB 정보 성공적으로 삭제")
                        callback(StatusCode.SUCCESS)
                    }
                    .addOnFailureListener { e ->
                        Log.w("FirestoreProductModel", "[${pid}] 상품 DB 정보 삭제 중 에러 발생!!! -> ", e)
                        callback(StatusCode.FAILURE)
                    }
            }
    }

    /**
     * Firestore에서 특정 ID의 상품 상세 정보를 조회합니다.
     *
     * @param pid 상세 정보를 조회할 상품의 ID입니다.
     * @param callback 사용자 정보 조회 상태 코드(STATUS_CODE)와 User 객체를 반환하는 콜백 함수입니다.
     */
    fun getProductsById(pid: String, callback:(Int, Product?) -> Unit){
        val getProduct = db.collection("Product").document(pid)
            getProduct.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()){
                    Log.d("FirestoreProductModel", "[${pid}] 상품 조회 성공")
                    val product = documentSnapshot.toObject(Product::class.java)
                    callback(StatusCode.SUCCESS, product)
                }else{
                    Log.d("FirestoreProductModel", "[${pid}] 상품이 DB에 존재하지 않음")
                    callback(StatusCode.FAILURE, null)
                }
            }
            .addOnFailureListener{e ->
                Log.w("FirestoreProductModel", "[${pid}] 상품을 DB에서 불러오는 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE, null)
            }
    }

    /**
     * Firestore에서 모든 상품의 상세 정보를 실시간으로 조회합니다.
     *
     * @param callback 상품 정보 조회 상태 코드(STATUS_CODE)와 상품 리스트를 반환하는 콜백 함수입니다.
     */
    fun getProducts(callback: (Int, List<Product>?) -> Unit) {
        db.collection("Product")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w("FirestoreProductModel", "전체 상품 목록을 DB에서 불러오는 중 에러 발생!!! -> ", e)
                    callback(StatusCode.FAILURE, null)
                    return@addSnapshotListener
                }

                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val productList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Product::class.java)
                    }
                    Log.d("FirestoreProductModel", "전체 상품 목록 DB에서 불러오기 성공")
                    callback(StatusCode.SUCCESS, productList)
                } else {
                    Log.d("FirestoreProductModel", "상품 목록이 DB에 존재하지 않음")
                    callback(StatusCode.SUCCESS, null)
                }
            }
    }

    /**
     * Firestore에서 상품 정보를 수정합니다.
     *
     * @param productId 상품의 고유 ID입니다.
     * @param updatedTitle 수정된 상품의 이름입니다.
     * @param updateImage 수정된 상품의 이미지 url입니다.
     * @param updatedDetail 수정된 상품의 상세 정보입니다.
     * @param updatedPrice 수정된 상품의 가격입니다.
     * @param updatedStateCode 수정된 상품의 상태 코드입니다.
     * @param callback 상품 정보 수정 상태 코드(STATUS_CODE)를 반환하는 콜백 함수입니다.
     */
    fun updateProduct(productId: String, updatedTitle: String, updateImage: String, updatedDetail: String, updatedPrice: Int, updatedStateCode: Int, callback: (Int) -> Unit) {
        val productRef = db.collection("Product").document(productId)
        val imageRef = FirebaseStorage.getInstance().reference.child("Product/${productId}")

        imageRef.metadata
            .addOnSuccessListener {
                imageRef.delete().addOnSuccessListener {
                    imageRef.putFile(Uri.parse(updateImage))
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val updatedData = hashMapOf("name" to updatedTitle, "img" to uri.toString(), "detail" to updatedDetail, "price" to updatedPrice, "stateCode" to updatedStateCode)
                                val updatedDataMap: Map<String, Any> = updatedData

                                productRef.update(updatedDataMap)
                                    .addOnSuccessListener {
                                        Log.d("FirestoreProductModel", "Storage에 이미지 수정 후 상품 수정 성공")
                                        callback(StatusCode.SUCCESS)
                                    }
                                    .addOnFailureListener { e->
                                        Log.w("FirestoreProductModel", "Storage에 이미지 수정 후 상품 수정 실패!!! -> ", e)
                                        callback(StatusCode.FAILURE)
                                    }
                            }
                        }
                }.addOnFailureListener { e->
                    Log.w("FirestoreProductModel", "수정전 이미지 Storage에 삭제 실패!!! -> ", e)
                }
            }.addOnFailureListener {
                imageRef.putFile(Uri.parse(updateImage))
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val updatedData = hashMapOf("name" to updatedTitle, "img" to uri.toString(), "detail" to updatedDetail, "price" to updatedPrice, "stateCode" to updatedStateCode)
                            val updatedDataMap: Map<String, Any> = updatedData

                            productRef.update(updatedDataMap)
                                .addOnSuccessListener {
                                    Log.d("FirestoreProductModel", "Storage에 이미지가 없던 상품 수정 성공")
                                    callback(StatusCode.SUCCESS)
                                }
                                .addOnFailureListener { e->
                                    Log.w("FirestoreProductModel", "Storage에 이미지가 없던 상품 수정 실패!!! -> ", e)
                                    callback(StatusCode.FAILURE)
                                }
                        }
                    }
            }

        val updatedData = hashMapOf("name" to updatedTitle, "detail" to updatedDetail, "price" to updatedPrice, "stateCode" to updatedStateCode)
        val updatedDataMap: Map<String, Any> = updatedData

        productRef.update(updatedDataMap)
            .addOnSuccessListener {
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener {
                callback(StatusCode.FAILURE)
            }
    }
}