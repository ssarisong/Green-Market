package kr.ac.hansung.greenmarket.models

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.ac.hansung.greenmarket.StatusCode

/**
 * Firestore를 사용하여 채팅과 채팅방을 관리하는 모델 클래스입니다.
 */
class FirestoreChattingModel {

    private val db = FirebaseFirestore.getInstance()

    /**
     * --내부 사용 함수--
     * 특정 사용자가 참여한 채팅방 목록을 가져옵니다.
     * 이 함수는 내부적으로 사용되며, 주어진 사용자 ID에 따라 채팅방을 검색합니다.
     *
     * @param userId 사용자 ID.
     * @param field 'sellerId' 또는 'buyerId' 중 하나를 나타내는 필드명.
     * @return 채팅방과 해당 Firestore 문서 ID의 Pair 목록.
     */
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

    /**
     * 사용자가 구매자 또는 판매자인 모든 채팅방을 조회합니다.
     *
     * @param userId 사용자 ID.
     * @return 채팅방 정보 요청 성공 시 상태 코드(StatusCode)와 채팅방 리스트를 인자로 받는 콜백 함수입니다.
     */
    suspend fun getChatRooms(userId: String): Pair<Int, List<ChatRoom>?> = withContext(Dispatchers.IO) {
        val sellerRoomsDeferred = async { getChatRoomsForUser(userId, "sellerId") }
        val buyerRoomsDeferred = async { getChatRoomsForUser(userId, "buyerId") }
        try {
            val sellerRooms = sellerRoomsDeferred.await()
            val buyerRooms = buyerRoomsDeferred.await()
            val allRooms = sellerRooms + buyerRooms

            val distinctChatRooms = allRooms
                .distinctBy { it.second }
                .map { it.first }
                .sortedByDescending { it.lastMessageAt }

            Log.d("firestoreChattingModel", "[$userId] 사용자의 채팅방 목록 불러오기 완료")
            StatusCode.SUCCESS to distinctChatRooms
        } catch (e: Exception) {
            Log.w("firestoreChattingModel", "[$userId] 사용자의 채팅방 목록 가져오기 실패!!! -> ", e)
            StatusCode.FAILURE to null
        }
    }

    /**
     * 특정 채팅방의 메시지를 실시간으로 감지합니다.
     *
     * @param chatRoomId 감지할 채팅방의 ID.
     * @param callback 새 메시지 또는 오류 발생 시 상태 코드(StatusCode)와 메시지 내용을 인자로 받는 콜백 함수입니다.
     * @return 리스너 등록을 해제하는 함수.
     */
    fun listenForMessages(chatRoomId: String, callback: (Int, List<Chat>?) -> Unit): () -> Unit {
        val chatMessagesRef = db.collection("ChatRoom").document(chatRoomId).collection("Chat")
        val query = chatMessagesRef.orderBy("createdAt", Query.Direction.ASCENDING)

        val registration = query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("FirestoreChattingModel", "[$chatRoomId] 메시지 실시간 감지 실패!!! -> ", e)
                callback(StatusCode.FAILURE, null)
                return@addSnapshotListener
            }
            if (snapshots != null && !snapshots.isEmpty) {
                val messages = snapshots.mapNotNull { it.toObject(Chat::class.java) }
                Log.d("firestoreChattingModel", "[$chatRoomId] 채팅방의 채팅 목록 불러오기 완료")
                callback(StatusCode.SUCCESS, messages)
            } else {
                Log.d("FirestoreChattingModel", "[$chatRoomId] 메시지가 아직 없음")
                callback(StatusCode.SUCCESS, emptyList())
            }
        }
        return { registration.remove() }
    }

    /**
     * 특정 채팅방에 메시지를 보냅니다.
     *
     * @param chatRoomId 메시지를 보낼 채팅방의 ID.
     * @param message 보낼 메시지 객체.
     * @param callback 메시지 전송 성공 또는 실패 시 상태 코드(StatusCode)를 인자로 받는 콜백 함수입니다.
     */
    fun sendMessage(chatRoomId: String, message: Chat, callback: (Int) -> Unit) {
        db.collection("ChatRoom").document(chatRoomId).collection("Chat")
            .add(message)
            .addOnSuccessListener {
                val chatRoomRef = db.collection("ChatRoom").document(chatRoomId)
                chatRoomRef.update("lastMessage", message.message, "lastMessageAt", message.createdAt)
                    .addOnSuccessListener {
                        Log.d("firestoreChattingModel", "[$chatRoomId]:메시지 내용[${message.message}]:송신자[${message.senderId}] 메시지 전송 완료")
                        callback(StatusCode.SUCCESS)
                    }
                    .addOnFailureListener { e ->
                        Log.w("FirestoreChattingModel", "[$chatRoomId]:메시지 내용[${message.message}]:송신자[${message.senderId}] lastMessage 업데이트가 실패!!! -> ", e)
                        callback(StatusCode.FAILURE)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreChattingModel", "[$chatRoomId]:메시지 내용[${message.message}]:송신자[${message.senderId}] 메시지 보내기 실패!!! -> ", e)
                callback(StatusCode.FAILURE)
            }
    }
    /**
     * Firestore에 새 채팅방을 생성합니다.
     *
     * @param chatRoom 채팅방 데이터를 가진 ChatRoom 객체입니다.
     * @param callback 채팅방 생성 성공 또는 실패 시 상태 코드(StatusCode)와 채팅방 ID를 인자로 받는 콜백 함수입니다.
     */
    fun createChatRoom(chatRoom: ChatRoom, callback: (Int, String) -> Unit) {
        val newChatRoomRef = db.collection("ChatRoom").document()

        val updatedChatRoom = chatRoom.copy(chatRoomId = newChatRoomRef.id)

        newChatRoomRef.set(updatedChatRoom)
            .addOnSuccessListener {
                Log.d("FirestoreChattingModel", "[${newChatRoomRef.id}] 신규 채팅방 성공적으로 생성")
                callback(StatusCode.SUCCESS, newChatRoomRef.id)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreChattingModel", "[${newChatRoomRef.id}] 채팅방 정보 생성 중 에러 발생!!! -> ", e)
                callback(StatusCode.FAILURE, newChatRoomRef.id)
            }
    }

    /**
     * 특정 채팅방의 모든 메시지를 조회하는 함수입니다.
     *
     * @param chatRoomId 메시지를 조회할 채팅방의 고유 ID입니다.
     * @param callback 메시지 목록 조회 성공 또는 실패 시 상태 코드(StatusCode)와 메시지 리스트(List<Chat>)를 인자로 받는 콜백 함수입니다.
     */
    suspend fun getAllMessages(chatRoomId: String, callback: (Int, List<Chat>?) -> Unit) = withContext(Dispatchers.IO) {
        try {
            val messages = db.collection("ChatRoom")
                .document(chatRoomId)
                .collection("Chat")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Chat::class.java) }
            Log.d("FirestoreChattingModel", "[${chatRoomId}] 채팅방의 메시지 목록 가져오기 완료")
            callback(StatusCode.SUCCESS, messages)
        } catch (e: Exception) {
            Log.w("FirestoreChattingModel", "[$chatRoomId] 채팅방의 메시지 목록 가져오기 실패!!! -> ", e)
            callback(StatusCode.FAILURE, null)
        }
    }

    /**
     * Firestore에 저장된 특정 채팅방의 정보를 조회합니다.
     *
     * @param chatRoomId 조회할 채팅방의 ID입니다.
     * @param callback 채팅방 정보 조회 성공 또는 실패 시 상태 코드(StatusCode)와 채팅방 정보를 인자로 받는 콜백 함수입니다.
     */
    fun getChatroomById(chatRoomId: String, callback: (Int, ChatRoom?) -> Unit) {
        db.collection("ChatRoom").document(chatRoomId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val chatRoom = document.toObject(ChatRoom::class.java)
                Log.d("FirestoreChattingModel", "[${chatRoomId}] ID값을 통한 채팅방 정보 조회 완료")
                callback(StatusCode.SUCCESS, chatRoom)
            } else {
                Log.d("FirestoreChatModel", "[$chatRoomId] 채팅방 정보 없음")
                callback(StatusCode.FAILURE, null)
            }
        }.addOnFailureListener { e ->
            Log.w("FirestoreChatModel", "[$chatRoomId] 채팅방 정보 조회 실패!!! -> ", e)
            callback(StatusCode.FAILURE, null)
        }
    }

    /**
     * Firestore의 특정 채팅방에 대한 lastMessage 변경을 실시간으로 감지합니다.
     *
     * @param chatRoomId 실시간으로 감지할 채팅방의 ID입니다.
     * @param callback lastMessage 정보를 반환하는 콜백 함수입니다.
     */
    fun listenForLastMessage(chatRoomId: String, callback: (Int, String?, Timestamp?) -> Unit) {
        db.collection("ChatRoom").document(chatRoomId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FirestoreChatModel", "[${chatRoomId}]채팅방 lastMessage 리스너 오류 발생!!! -> ", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d("FirestoreChatModel", "[${chatRoomId}] 채팅방의 마지막 메시지와 보낸 시간 실시간 불러오기 완료")
                    callback(StatusCode.SUCCESS, snapshot.getString("lastMessage"), snapshot.getTimestamp("lastMessageAt"))
                } else {
                    Log.d("FirestoreChatModel", "[${chatRoomId}] 채팅방의 lastMessage 데이터 없음")
                    callback(StatusCode.FAILURE, null, null)
                }
            }
    }

    /**
     * 두 사용자가 참여하는 채팅방을 조회합니다.
     *
     * @param userId 첫 번째 사용자의 ID.
     * @param partnerId 두 번째 사용자의 ID.
     * @return 채팅방이 존재하면 StatusCode.SUCCESS와 해당 채팅방의 ID를,
     *         존재하지 않으면 StatusCode.SUCCESS와 null을,
     *         오류가 발생하면 StatusCode.FAILURE와 null을 반환합니다.
     */
    suspend fun getChatRoomIdBetweenUsers(userId: String, partnerId: String): Pair<Int, String?> = withContext(Dispatchers.IO) {
        try {
            val chatRooms = db.collection("ChatRoom")
                .whereEqualTo("sellerId", userId)
                .whereEqualTo("buyerId", partnerId)
                .get()
                .await()
                .documents

            if (chatRooms.isNotEmpty()) {
                return@withContext StatusCode.SUCCESS to chatRooms.first().id
            }

            // userId와 partnerId의 위치를 바꿔서 검색
            val chatRoomsReversed = db.collection("ChatRoom")
                .whereEqualTo("sellerId", partnerId)
                .whereEqualTo("buyerId", userId)
                .get()
                .await()
                .documents

            if (chatRoomsReversed.isNotEmpty()) {
                return@withContext StatusCode.SUCCESS to chatRoomsReversed.first().id
            }

            // 채팅방이 존재하지 않는 경우, 성공적으로 조회했으나 찾지 못한 것으로 간주
            StatusCode.SUCCESS to null
        } catch (e: Exception) {
            Log.w("FirestoreChattingModel", "[$userId]와 [$partnerId] 사이의 채팅방 조회 실패!!! -> ", e)
            StatusCode.FAILURE to null
        }
    }
}