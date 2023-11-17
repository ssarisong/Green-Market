package kr.ac.hansung.greenmarket.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.StatusCode
import kr.ac.hansung.greenmarket.models.ChatRoom
import kr.ac.hansung.greenmarket.models.User
import kr.ac.hansung.greenmarket.utils.FirebaseChattingUtil
import kr.ac.hansung.greenmarket.utils.FirebaseUserUtil
import java.text.SimpleDateFormat
import java.util.Locale

class ChatListAdapter(
    private val chatRooms: List<ChatRoom>,
    private val onItemClick: (ChatRoom) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatRoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.bind(chatRoom)

        // 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            onItemClick(chatRoom)
        }
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    inner class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userUtil = FirebaseUserUtil()
        private val chatUtil = FirebaseChattingUtil()

        private val imageView: ImageView = itemView.findViewById(R.id.item_chat_imageView)
        private val txtTitle: TextView = itemView.findViewById(R.id.item_chat_tv_title)
        private val txtComment: TextView = itemView.findViewById(R.id.item_chat_tv_comment)
        private val txtTimestamp: TextView = itemView.findViewById(R.id.item_chat_tv_timestamp)

        fun bind(chatRoom: ChatRoom) {
            chatUtil.whoIsMyPartner(chatRoom.chatRoomId, userUtil.whoAmI()?.uid) { myPartnerId ->
                if(myPartnerId != null){
                    userUtil.getUser(myPartnerId) { STATUS_CODE, myPartner ->
                        if (STATUS_CODE == StatusCode.SUCCESS) {
                            if (myPartner != null) {
                                txtTitle.text = myPartner.name
                            }
                        }
                    }
                }
            }

            chatUtil.listenForLastMessage(chatRoom.chatRoomId) { STATUS_CODE, lastMessage ->
                txtComment.text = lastMessage ?: "아직 대화한 기록이 없습니다."
            }
            txtTimestamp.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(chatRoom.createdAt.toDate()) // You can set timestamp if needed

            // Set user image in imageView (You need to add logic to fetch and set the image)
            // Glide or Picasso can be used for loading images.
            // Example: Glide.with(itemView.context).load(chatRoom.imageUrl).into(imageView)
        }
    }
}

