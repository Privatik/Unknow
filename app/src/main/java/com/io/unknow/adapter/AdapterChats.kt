package com.io.unknow.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.R
import com.io.unknow.model.Chat
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.ui.dialogfragment.DialogWithUserFragment

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

    class ItemChat(itemView: View,val fragment: Fragment) : RecyclerView.ViewHolder(itemView) , View.OnClickListener{
        private val id = itemView.findViewById<TextView>(R.id.id)
        private val message = itemView.findViewById<TextView>(R.id.message)
        private var messageId: String = ""
        private var userId: String = ""
        private lateinit var chat: Chat

        init {
            itemView.findViewById<ConstraintLayout>(R.id.content).setOnClickListener(this)
        }

        fun initer(userId: String, chat: Chat){
            id.text = userId

            if (messageId != chat.messages){
                messageId = chat.messages
                this.userId = userId
                this.chat = chat
            }

            if (chat.last_message != null){
                if (userId == chat.last_message?.userId)
                    message.text = chat.last_message?.text
                else{
                    message.text = "Вы:  " +  chat.last_message?.text
                }
            }
        }

        override fun onClick(v: View?) {
            fragment.startActivity(DialogActivity.newInstance(fragment.requireContext(), chat, userId))
        }
    }
}