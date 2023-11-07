package kr.ac.hansung.greenmarket.models

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import kr.ac.hansung.greenmarket.StatusCode

/**
 * Firestore를 사용하여 사용자 데이터를 관리하는 모델 클래스입니다.
 */
class FirestoreUserModel {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Firestore에 사용자를 추가합니다.
     *
     * @param uid 사용자의 고유 ID입니다.
     * @param user 사용자 정보를 담고 있는 User 객체입니다.
     * @param callback 상태 코드(STATUS_CODE)를 반환하는 콜백 함수입니다.
     */
    fun insertUser(uid: String, user: User, callback: (Int) -> Unit) {
        db.collection("User").document(uid).set(user)
            .addOnSuccessListener {
                Log.d("FirestoreUserModel", "${user.name}(${user.email}) 사용자 DB 정보 성공적으로 생성 -> ID: [${uid}]")
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "사용자 DB 정보 생성 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE)
            }
    }

    /**
     * Firestore에서 특정 사용자를 삭제합니다.
     *
     * @param uid 상세 정보를 조회할 사용자의 고유 ID입니다.
     * @param callback 사용자 정보 조회 상태 코드(STATUS_CODE)와 User 객체를 반환하는 콜백 함수입니다.
     */
    fun deleteUser(uid: String, callback: (Int) -> Unit) {
        db.collection("User").document(uid).delete()
            .addOnSuccessListener {
                Log.d("FirestoreUserModel", "ID: [${uid}] 사용자 DB 정보 성공적으로 삭제")
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "ID: [${uid}] 사용자 DB 정보 삭제 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE)
            }
    }

    /**
     * Firestore에서 특정 사용자의 상세 정보를 조회합니다.
     *
     * @param uid 상세 정보를 조회할 사용자의 고유 ID입니다.
     * @param callback 사용자 정보 조회 상태 코드(STATUS_CODE)와 User 객체를 반환하는 콜백 함수입니다.
     */
    fun getUserDetail(uid: String,  callback: (Int, User?) -> Unit) {
        db.collection("User").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("FirestoreUserModel", "ID: [${uid}] 사용자 DB에 불러오기 성공")
                    val user = documentSnapshot.toObject(User::class.java)
                    callback(StatusCode.SUCCESS, user)
                } else {
                    Log.d("FirestoreUserModel", "ID: [${uid}] 사용자가 DB에 존재하지 않음")
                    callback(StatusCode.FAILURE, null)
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "ID: [${uid}] 사용자를 DB에서 불러오는 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE, null)
            }
    }
}
