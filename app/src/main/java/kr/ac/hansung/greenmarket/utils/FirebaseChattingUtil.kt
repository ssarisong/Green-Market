package kr.ac.hansung.greenmarket.utils

import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.Chat
import kr.ac.hansung.greenmarket.models.ChatRoom
import kr.ac.hansung.greenmarket.models.FirestoreChattingModel

/**
 * Firebase 채팅 관련 작업을 처리하는 유틸리티 클래스입니다.
 */
class FirebaseChattingUtil {

    private val chattingModel = FirestoreChattingModel()

    /**
     * 새로운 채팅방을 생성합니다.
     *
     * @param chatRoomId 생성할 채팅방의 ID입니다.
     * @param callback 채팅방 생성 성공 시 상태 코드(StatusCode)와 채팅방 ID를 인자로 받는 콜백 함수입니다.
     */
    fun createChatRoom(chatRoomId: String, callback: (Int, String?) -> Unit) {
        // 채팅방 생성 로직 구현
    }

    /**
     * 채팅방에 메시지를 전송합니다.
     *
     * @param userId 메시지를 보내는 사용자의 식별자입니다.
     * @param chatRoomId 메시지를 보낼 채팅방의 ID입니다.
     * @param message 전송할 메시지입니다.
     * @param callback 메시지 전송 성공 시 상태 코드(StatusCode)를 인자로 받는 콜백 함수입니다.
     */
    fun sendMessage(userId: String, chatRoomId: String, message: String, callback: (Int) -> Unit) {
        val newChat = Chat(chatRoomId, userId, message, Timestamp.now())
        chattingModel.sendMessage(chatRoomId, newChat) { statusCode ->
            callback(statusCode)
        }
    }

    /**
     * 채팅방의 메시지를 실시간으로 감지하는 리스너를 설정합니다.
     *
     * @param chatRoomId 메시지를 감지할 채팅방의 ID입니다.
     * @param callback 메시지 감지 성공 또는 실패 시 상태 코드(StatusCode)와 메시지 리스트를 인자로 받는 콜백 함수입니다.
     * @return 리스너를 해제하는 함수를 반환합니다.
     */
    fun listenForMessages(chatRoomId: String, callback: (Int, List<Chat>?) -> Unit): () -> Unit {
        return chattingModel.listenForMessages(chatRoomId, callback)
    }

    /**
    * 사용자의 채팅방 정보를 반환합니다.
    *
    * @param userId 채팅방 정보를 요청하는 사용자의 식별자입니다.
    * @param callback 채팅방 정보 요청 성공 시 상태 코드(StatusCode)와 채팅방 리스트를 인자로 받는 콜백 함수입니다.
    */
    fun getUserChatRooms(userId: String, callback: (Int, List<ChatRoom>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val (statusCode, chatRooms) = chattingModel.getChatRooms(userId)
            callback(statusCode, chatRooms)
        }
    }
}
