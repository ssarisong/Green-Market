package kr.ac.hansung.greenmarket.models

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class FirestoreUserModel {
    val db = FirebaseFirestore.getInstance()

    companion object {
        const val SUCCESS = 1
        const val FAILURE = 0
    }

    fun insertUser(uid: String, user: User, callback: (Int) -> Unit) {
        db.collection("User").document(uid).set(user)
            .addOnSuccessListener {
                Log.d("FirestoreUserModel", "${user.NAME}(${user.EMAIL}) 사용자 DB 정보 성공적으로 생성 -> ID: [${uid}]")
                callback(SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "사용자 DB 정보 생성 중 에러 발생!!! -> ", e)
                callback(FAILURE)
            }
    }

    fun deleteUser(uid: String, callback: (Int) -> Unit) {
        db.collection("User").document(uid).delete()
            .addOnSuccessListener {
                Log.d("FirestoreUserModel", "ID: [${uid}] 사용자 DB 정보 성공적으로 삭제")
                callback(SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "ID: [${uid}] 사용자 DB 정보 삭제 중 에러 발생!!! -> ", e)
                callback(FAILURE)
            }
    }

    fun getUserDetail(uid: String,  callback: (Int, User?) -> Unit) {
        db.collection("User").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    callback(SUCCESS, user)
                } else {
                    Log.d("FirestoreUserModel", "ID: [${uid}] 사용자가 DB에 존재하지 않음")
                    callback(FAILURE, null)
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreUserModel", "ID: [${uid}] 사용자를 DB에서 불러오는 중 에러 발생!!! -> ", e)
                callback(FAILURE, null)
            }
    }
}
