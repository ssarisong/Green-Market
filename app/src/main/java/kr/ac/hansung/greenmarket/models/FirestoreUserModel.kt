package kr.ac.hansung.greenmarket.models

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import kr.ac.hansung.greenmarket.StatusCode

class FirestoreUserModel {
    private val db = FirebaseFirestore.getInstance()

    fun insertUser(uid: String, user: User, callback: (Int) -> Unit) {
        db.collection("User").document(uid).set(user)
            .addOnSuccessListener {
                Log.d("FirestoreUserModel", "${user.NAME}(${user.EMAIL}) 사용자 DB 정보 성공적으로 생성 -> ID: [${uid}]")
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "사용자 DB 정보 생성 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE)
            }
    }

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
