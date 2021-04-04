package com.io.unknow.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.R
import com.io.unknow.firebase.ChatListModel
import com.io.unknow.firebase.ChatListener
import com.io.unknow.livedata.ChatLiveData
import com.io.unknow.model.Chat
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.ui.dialogfragment.EditChatDialogFragment
import com.io.unknow.ui.dialogfragment.EditMessageDialogFragment

class AdapterChats(private val map: MutableMap<String, Chat>,val fragment: Fragment) : RecyclerView.Adapter<AdapterChats.ItemChat>() {

    private var setUserId: Set<String> = map.keys

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChat =
        ItemChat(LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false),fragment)

    override fun onBindViewHolder(holder: ItemChat, position: Int) {
        setUserId.elementAt(position).let { map[it]?.let { it1 -> holder.initer(it, it1) } }
    }

    fun updateList(newMap: MutableMap<String, Chat>){
        map.clear()
        map.putAll(newMap)
        setUserId = map.keys
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = position


    override fun getItemCount(): Int = map.size

    class ItemChat(itemView: View,val fragment: Fragment) : RecyclerView.ViewHolder(itemView){
        private val id = itemView.findViewById<TextView>(R.id.id)
        private val message = itemView.findViewById<TextView>(R.id.message)
        private var messageId: String = ""
        private var userId: String = ""
        private lateinit var chat: Chat

        init {
            itemView.setOnClickListener {
                fragment.startActivity(DialogActivity.newInstance(fragment.requireContext(), chat, userId))
            }

            itemView.setOnLongClickListener {
                Log.e("LongClick","Click")
                EditChatDialogFragment.newInstance(chat = chat).show(fragment.childFragmentManager,"delete")
                return@setOnLongClickListener true
            }
        }

        @SuppressLint("SetTextI18n")
        fun initer(userId: String, chat: Chat) {
            id.text = userId

            if (messageId != chat.messages) {
                messageId = chat.messages
                this.userId = userId
                this.chat = chat
            }

            val liveData = ChatLiveData()
            ChatListener(userId = userId, chat = chat, liveData = liveData).newMessageListener()


            liveData.observe(fragment.viewLifecycleOwner) {
                 message.text = it
            }
        }
    }
}