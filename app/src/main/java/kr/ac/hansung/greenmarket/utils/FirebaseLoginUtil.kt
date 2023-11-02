package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.hansung.greenmarket.models.FirestoreUserModel
import kr.ac.hansung.greenmarket.models.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirebaseLoginUtil{

    companion object {
        const val SUCCESS = 1
        const val FAILURE = 0
    }

    val model = FirestoreUserModel()

    fun doLogin(userEmail: String, password: String, callback: (Int, String?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = Firebase.auth.currentUser?.uid.toString()
                    Log.d("FirebaseLoginUtils", "ID: [${uid}] 사용자 로그인 성공")
                    callback(SUCCESS, uid)
                } else {
                    Log.w("FirebaseLoginUtils", "EMAIL: [${userEmail}] 사용자 로그인 실패!!! -> ", task.exception)
                    callback(FAILURE, null)
                }
            }
    }

    fun doSignup(userEmail: String, password: String, name: String, birth: String, callback: (Int, String?) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val uid = Firebase.auth.currentUser?.uid.toString()
                    val birthDate = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val newUser = User(userEmail, password, name, birthDate, LocalDate.now())
                    model.insertUser(uid, newUser) { result ->
                        if (result == FirestoreUserModel.SUCCESS) {
                            Log.d("FirebaseLoginUtils", "Auth에서 사용자 성공적으로 생성 -> ID: [${uid}]")
                            callback(SUCCESS, uid)
                        } else {
                            callback(FAILURE, uid)
                        }
                    }
                }else{
                    Log.w("FirebaseLoginUtils", "Auth에서 사용자 생성 중 에러 발생!!! -> ", task.exception)
                    callback(FAILURE, null)
                }
            }
    }
}