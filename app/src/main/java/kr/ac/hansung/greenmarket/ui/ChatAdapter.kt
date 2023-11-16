package kr.ac.hansung.greenmarket.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hansung.greenmarket.R
import kr.ac.hansung.greenmarket.models.Chat
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(private val chatList: MutableList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message1, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addChat(chat: Chat) {
        chatList.add(chat)
        notifyItemInserted(chatList.size - 1)
    }

    fun addChatList(messages: List<Chat>) {
        chatList.addAll(messages)
        notifyDataSetChanged()
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtMessage: TextView = itemView.findViewById(R.id.txt_message)
        private val txtDate: TextView = itemView.findViewById(R.id.txt_date)

        fun bind(chat: Chat) {
            txtMessage.text = chat.message

            // Timestamp를 문자열로 변환하여 설정
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedDate = dateFormat.format(chat.createdAt.toDate())
            txtDate.text = formattedDate
        }
    }
}
