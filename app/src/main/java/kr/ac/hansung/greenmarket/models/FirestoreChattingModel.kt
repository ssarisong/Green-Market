package kr.ac.hansung.greenmarket.models

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.ac.hansung.greenmarket.StatusCode

class FirestoreChattingModel {
    private val db = FirebaseFirestore.getInstance()

    private suspend fun getChatRoomsForUser(userId: String, field: String) = db.collection("ChatRoom")
        .whereEqualTo(field, userId)
        .get()
        .await()
        .documents
        .mapNotNull {
            it.toObject(ChatRoom::class.java)?.let { chatRoom ->
                chatRoom to it.id
            }
        }

    suspend fun getChatRooms(userId: String): Pair<Int, List<ChatRoom>?> = withContext(Dispatchers.IO) {
        coroutineScope {
            val sellerRoomsDeferred = async { getChatRoomsForUser(userId, "sellerId") }
            val buyerRoomsDeferred = async { getChatRoomsForUser(userId, "buyerId") }
            try {
                val sellerRooms = sellerRoomsDeferred.await()
                val buyerRooms = buyerRoomsDeferred.await()
                val allRooms = sellerRooms + buyerRooms

                val distinctChatRooms = allRooms
                    .distinctBy { it.second }
                    .map { it.first }

                StatusCode.SUCCESS to distinctChatRooms
            } catch (e: Exception) {
                Log.w("FirestoreChattingModel", "채팅방 목록 가져오기 실패: ", e)
                StatusCode.FAILURE to null
            }
        }
    }

    fun listenForMessages(chatRoomId: String, callback: (Int, List<Chat>?) -> Unit): () -> Unit {
        val chatMessagesRef = db.collection("ChatRoom").document(chatRoomId).collection("messages")
        val query = chatMessagesRef.orderBy("createdAt", Query.Direction.ASCENDING)

        val registration = query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("FirestoreChattingModel", "메시지 실시간 감지 실패: ", e)
                callback(StatusCode.FAILURE, null)
                return@addSnapshotListener
            }
            if (snapshots != null && !snapshots.isEmpty) {
                val messages = snapshots.mapNotNull { it.toObject(Chat::class.java) }
                callback(StatusCode.SUCCESS, messages)
            } else {
                callback(StatusCode.FAILURE, null)
            }
        }
        return { registration.remove() }
    }

    fun sendMessage(chatRoomId: String, message: Chat, callback: (Int) -> Unit) {
        db.collection("ChatRoom").document(chatRoomId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                callback(StatusCode.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreChattingModel", "메시지 보내기 실패: ", e)
                callback(StatusCode.FAILURE)
            }
    }
}