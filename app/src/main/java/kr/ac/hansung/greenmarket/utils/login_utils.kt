package kr.ac.hansung.greenmarket.utils

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.ui.MainActivity

class login_utils : AppCompatActivity(){
    fun doLogin(userEmail: String, password: String){
        val userEmail = findViewById<EditText>(R.id.editText_id)?.text.toString()
        val password = findViewById<EditText>(R.id.editText_pw)?.text.toString()
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun doSignup (userEmail: String, password: String, name: String, birth: String){
        val userEmail = findViewById<EditText>(R.id.editText_signup_id)?.text.toString()
        val password = findViewById<EditText>(R.id.editText_signup_pw)?.text.toString()
        val name = findViewById<EditText>(R.id.editText_signup_name)?.text.toString()
        val birth = findViewById<EditText>(R.id.editText_signup_birth)?.text.toString()

        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Log.w("LoginActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}