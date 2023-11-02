package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseLoginUtils{
    fun doLogin(userEmail: String, password: String, callback: (String?) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = Firebase.auth.currentUser?.uid
                    callback(uid)
                } else {
                    Log.w("FirebaseLoginUtils", "doLogin", task.exception)
                    callback(null)
                }
            }
    }

    fun doSignup(userEmail: String, password: String, name: String, birth: String, callback: (String?) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val uid = Firebase.auth.currentUser?.uid
                    callback(uid)
                }else{
                    Log.w("FirebaseLoginUtils", "doSignup", task.exception)
                    callback(null)
                }
            }
    }
}