package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.FirestoreUserModel
import kr.ac.hansung.greenmarket.models.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirebaseUserUtil{

    private val userModel = FirestoreUserModel()

    fun doSignIn(userEmail: String, password: String, callback: (Int, String?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = Firebase.auth.currentUser?.uid.toString()
                    Log.d("FirebaseLoginUtils", "ID: [${uid}] 사용자 로그인 성공")
                    callback(StatusCode.SUCCESS, uid)
                } else {
                    Log.w("FirebaseLoginUtils", "EMAIL: [${userEmail}] 사용자 로그인 실패!!! -> ", task.exception)
                    callback(StatusCode.FAILURE, null)
                }
            }
    }

    fun doSignUp(userEmail: String, password: String, name: String, birth: String, callback: (Int, String?) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val uid = Firebase.auth.currentUser?.uid.toString()
                    val birthDate = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val newUser = User(userEmail, password, name, birthDate, LocalDate.now())
                    userModel.insertUser(uid, newUser) { STATUS_CODE ->
                        if (STATUS_CODE == StatusCode.SUCCESS) {
                            Log.d("FirebaseLoginUtils", "Auth에서 사용자 성공적으로 생성 -> ID: [${uid}]")
                            callback(StatusCode.SUCCESS, uid)
                        } else {
                            callback(StatusCode.FAILURE, uid)
                        }
                    }
                }else{
                    Log.w("FirebaseLoginUtils", "Auth에서 사용자 생성 중 에러 발생!!! -> ", task.exception)
                    callback(StatusCode.FAILURE, null)
                }
            }
    }

    fun doSignOut(callback: (Int) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser == null) {
            Log.d("FirebaseLoginUtils", "ID: [${uid}] 사용자 로그아웃 성공")
            callback(StatusCode.SUCCESS)
        } else {
            Log.w("FirebaseLoginUtils", "ID: [${uid}] 사용자 로그아웃 실패...")
            callback(StatusCode.FAILURE)
        }
    }

    fun getUser(uid: String, callback: (Int, User?) -> Unit) {
        userModel.getUserDetail(uid) { STATUS_CODE, user ->
            if (STATUS_CODE == StatusCode.SUCCESS) {
                callback(StatusCode.SUCCESS, user)
            } else {
                callback(StatusCode.FAILURE, null)
            }
        }
    }
}