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
     * 새 채팅방을 생성합니다.
     *
     * @param productId 해당 상품의 ID.
     * @param buyerId 구매희망자의 사용자 ID.
     * @param sellerId 판매자의 사용자 ID.
     * @param callback 채팅방 생성 성공 시 상태 코드(StatusCode)와 채팅방 리스트를 인자로 받는 콜백 함수입니다.
     */
    fun createChatRoom(productId: String, buyerId: String, sellerId: String, callback: (Int, String?) -> Unit) {
        val newChatRoom = ChatRoom(chatRoomId = "", productId = productId, buyerId = buyerId, sellerId = sellerId, createdAt = Timestamp.now())

        chattingModel.createChatRoom(newChatRoom) { status, chatRoomId ->
            if (status == StatusCode.SUCCESS) {
                callback(StatusCode.SUCCESS, chatRoomId)
            } else {
                callback(StatusCode.FAILURE, null)
            }
        }
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
        val newChat = Chat(userId, message, Timestamp.now())
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
     * 마지막 메시지 및 그 시간을 실시간으로 감지하는 리스너를 설정합니다.
     *
     * @param chatRoomId 마지막 메시지를 감지할 채팅방의 ID입니다.
     * @param callback 마지막 메시지 감지 성공 또는 실패 시 상태 코드(StatusCode), 마지막 메시지, 마지막 메시지를 보낸 시간을 인자로 받는 콜백 함수입니다.
     */
    fun listenForLastMessage(chatRoomId: String, callback: (Int, String?, Timestamp?) -> Unit) {
        chattingModel.listenForLastMessage(chatRoomId) { statusCode, lastMessage, lastMessageAt ->
            callback(statusCode, lastMessage, lastMessageAt)
        }
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

    /**
     * 특정 채팅방의 모든 메시지를 가져옵니다.
     *
     * @param chatRoomId 메시지를 조회할 채팅방의 ID입니다.
     * @param callback 메시지 목록 조회 성공 시 상태 코드(StatusCode)와 메시지 리스트를 인자로 받는 콜백 함수입니다.
     */
    suspend fun fetchAllMessages(chatRoomId: String, callback: (Int, List<Chat>?) -> Unit) {
        chattingModel.getAllMessages(chatRoomId) { statusCode, chats ->
            callback(statusCode, chats)
        }
    }

    /**
     * 특정 채팅방에서 사용자의 파트너(상대방)의 ID를 조회합니다.
     *
     * @param chatRoomId 조회할 채팅방의 ID입니다.
     * @param userId 사용자의 ID입니다. 이 ID를 기반으로 파트너를 식별합니다.
     * @param callback 파트너 ID 조회에 성공하거나 실패했을 때 파트너 ID를 인자로 받는 콜백 함수입니다.
     */
    fun whoIsMyPartner(chatRoomId: String, userId: String?, callback: (String?) -> Unit) {
        chattingModel.getChatroomById(chatRoomId) { STATUS_CODE, myChatRoom ->
            if (STATUS_CODE == StatusCode.SUCCESS) {
                val partnerId = if (myChatRoom != null) {
                    if (myChatRoom.buyerId == userId) myChatRoom.sellerId else myChatRoom.buyerId
                } else null
                callback(partnerId)
            } else {
                callback(null)
            }
        }
    }

    /**
     * 채팅방 생성 전 이미 채팅방이 만들어져 있는 대상인지 확인하고, 맞다면 이미 만들어져 있는 채팅방의 ID값을 불러옵니다.
     *
     * @param userId 현재 사용자의 ID입니다.
     * @param partnerId 현재 채팅방을 만드려고 하는 파트너의 ID입니다.
     * @return 조회 성공 여부 코드(first)와 채팅방이 있을 시 해당 채팅방 ID 없으면 null을 반환합니다.
     */
    suspend fun checkChatroomAlreadyExist(userId: String, partnerId: String): Pair<Int, String?> {
        return chattingModel.getChatRoomIdBetweenUsers(userId, partnerId)
    }
}
