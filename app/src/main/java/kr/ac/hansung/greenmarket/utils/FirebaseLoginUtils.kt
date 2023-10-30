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
                    Log.w("LoginActivity", "signInWithEmail", task.exception)
                    callback(null)
                }
            }
    }

//    fun doSignup (userEmail: String, password: String, name: String, birth: String){
//        val userEmail = findViewById<EditText>(R.id.editText_signup_id)?.text.toString()
//        val password = findViewById<EditText>(R.id.editText_signup_pw)?.text.toString()
//        val name = findViewById<EditText>(R.id.editText_signup_name)?.text.toString()
//        val birth = findViewById<EditText>(R.id.editText_signup_birth)?.text.toString()
//
//        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
//            .addOnCompleteListener(this){
//                if(it.isSuccessful){
//                    startActivity(
//                        Intent(this, MainActivity::class.java))
//                    finish()
//                }else{
//                    Log.w("LoginActivity", "signUpWithEmail", it.exception)
//                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}