package com.io.unknow.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.ChatLiveData
import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageText
import com.io.unknow.parse.MessageParse
import javax.inject.Inject

private const val CHAT = "chats"
class ChatListener(private val userId: String, private val chat: Chat, private val liveData: ChatLiveData){

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference

    private lateinit var baseChat: DatabaseReference

    init {
        App.appComponent.inject(this)

        baseChat = base.child(CHAT).child(mAuth.currentUser!!.uid).child(userId)
    }

    fun newMessageListener(){
        baseChat.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(value: DataSnapshot) {
                val chat = Chat(messages = value.child("messages").getValue(String::class.java)!!,
                    last_message = MessageParse.getMessageFromShot(value.child("last_message")),
                    readNow = value.child("readNow").getValue(Boolean::class.java)!!)

                if (chat.last_message != null) {
                    val isMyMessage = chat.last_message!!.userId != userId
                    liveData.setMessage(if (isMyMessage) " ${message(chat.last_message!!)}"
                    else "Вы: ${message(chat.last_message!!)}")
                }
                else{
                    liveData.setMessage("Empty")
                }
            }

        })
    }

    private fun message(message: IMessage):String =  if (message is MessageText) message.text else "Фотография"



}