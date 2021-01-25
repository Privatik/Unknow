package com.io.unknow.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.io.unknow.app.App
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.livedata.TwoFieldLiveData
import com.io.unknow.model.Chat
import java.util.*
import javax.inject.Inject

class CreateChatModel(private val databaseReference: DatabaseReference) {

    fun createChat(uuidMessage: String, id: String, userId: String){
        val chat = Chat(uuidMessage)
        databaseReference.child("chats").child(id).child(userId).setValue(chat)
        CurrentUser.map[userId] = chat
    }
}