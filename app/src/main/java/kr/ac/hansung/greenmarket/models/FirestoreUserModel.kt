package kr.ac.hansung.greenmarket.models

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class FirestoreUserModel {
    val db = FirebaseFirestore.getInstance()

    fun insertUser(uid: String, user: User) {
        db.collection("User").document(uid).set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "${user.NAME}(${user.EMAIL}) added with ID: ${uid}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }
}
