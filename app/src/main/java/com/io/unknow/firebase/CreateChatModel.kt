package com.io.unknow.firebase

import com.google.firebase.database.DatabaseReference
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.Chat

class CreateChatModel(private val databaseReference: DatabaseReference) {

    fun createChat(uuidMessage: String, id: String, userId: String){
        val chat = Chat(uuidMessage)
        databaseReference.child("chats").child(id).child(userId).setValue(chat)
        CurrentUser.map[userId] = chat
    }
}