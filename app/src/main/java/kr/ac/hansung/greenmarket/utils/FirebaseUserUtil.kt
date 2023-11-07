package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.FirestoreUserModel
import kr.ac.hansung.greenmarket.models.User
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Firebase 인증 관련 작업을 처리하는 유틸리티 클래스입니다.
 * 로그인, 회원가입, 로그아웃, 사용자 정보 조회 기능을 제공합니다.
 */
class FirebaseUserUtil{

    private val userModel = FirestoreUserModel()

    /**
     * 이메일과 비밀번호를 사용하여 사용자 로그인을 수행합니다.
     *
     * @param userEmail 로그인할 사용자의 이메일입니다.
     * @param password 로그인할 사용자의 비밀번호입니다.
     * @param callback 로그인 성공 시 상태 코드(STATUS_CODE)와 사용자 ID를 인자로 받는 콜백 함수입니다.
     */
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

    /**
     * 이메일, 비밀번호, 이름, 생년월일을 사용하여 새로운 사용자를 등록합니다.
     *
     * @param userEmail 새로운 사용자의 이메일입니다.
     * @param password 새로운 사용자를 위한 비밀번호입니다.
     * @param name 새로운 사용자의 이름입니다.
     * @param birth 새로운 사용자의 생년월일로 "yyyy-MM-dd" 형식입니다.
     * @param callback 회원가입 성공 시 상태 코드(STATUS_CODE)와 사용자 ID를 인자로 받는 콜백 함수입니다.
     */
    fun doSignUp(userEmail: String, password: String, name: String, birth: String, callback: (Int, String?) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener{additionTask ->
                if(additionTask.isSuccessful){
                    val uid = Firebase.auth.currentUser?.uid.toString()
                    val currentUser = Firebase.auth.currentUser
                    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val birthDate = Timestamp(parser.parse(birth))
                    val newUser = User(userEmail, name, birthDate, Timestamp.now())
                    userModel.insertUser(uid, newUser) { STATUS_CODE ->
                        if (STATUS_CODE == StatusCode.SUCCESS) {
                            Log.d("FirebaseLoginUtils", "Auth에서 사용자 성공적으로 생성 -> ID: [${uid}]")
                            callback(StatusCode.SUCCESS, uid)
                        } else {
                            currentUser?.delete()
                                ?.addOnCompleteListener { deletionTask ->
                                    if (deletionTask.isSuccessful) {
                                        Log.d("FirebaseLoginUtils", "Auth 계정 삭제 성공 -> ID: [${uid}]")
                                    } else {
                                        Log.w("FirebaseLoginUtils", "Auth 계정 삭제 실패!! -> ", deletionTask.exception)
                                    }
                                }
                            callback(StatusCode.FAILURE, null)
                        }
                    }
                }else{
                    Log.w("FirebaseLoginUtils", "Auth에서 사용자 생성 중 에러 발생!!! -> ", additionTask.exception)
                    callback(StatusCode.FAILURE, null)
                }
            }
    }

    /**
     * 현재 로그인된 사용자를 로그아웃합니다.
     *
     * @param callback 로그아웃 결과를 나타내는 상태 코드(STATUS_CODE)를 인자로 받는 콜백 함수입니다.
     */
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

    /**
     * UID를 사용하여 사용자 객체를 검색합니다.
     *
     * @param uid 검색할 사용자의 UID입니다.
     * @param callback 사용자 정보 조회 성공 시 상태 코드(STATUS_CODE)와 사용자 객체를 인자로 받는 콜백 함수입니다.
     */
    fun getUser(uid: String, callback: (Int, User?) -> Unit) {
        userModel.getUserDetail(uid) { STATUS_CODE, user ->
            if (STATUS_CODE == StatusCode.SUCCESS) {
                callback(StatusCode.SUCCESS, user)
            } else {
                callback(StatusCode.FAILURE, null)
            }
        }
    }

    fun whoAmI(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}