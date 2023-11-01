package kr.ac.hansung.greenmarket.utils

import android.util.Log
import com.example.greenmarket.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login_signup {
    fun doSignup(userEmail: String, password: String, name: String, birth: String, callback: (String?) -> Unit){
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val uid = Firebase.auth.currentUser?.uid
                    callback(null)
                }else{
                    Log.w("LoginActivity", "signUpWithEmail", task.exception)
                    callback(null)
                }
            }
    }
}