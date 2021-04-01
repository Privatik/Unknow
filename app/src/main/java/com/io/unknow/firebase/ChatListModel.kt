package com.io.unknow.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.io.unknow.app.App
import com.io.unknow.model.Chat
import javax.inject.Inject

class ChatListModel {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference

    private val baseChat: DatabaseReference

    init {
        App.appComponent.inject(this)

        baseChat = base.child("chats").child(mAuth.currentUser!!.uid)
    }


    fun deleteChat(userId: String){
        baseChat.child(userId).removeValue()
        base.child(userId).child(mAuth.currentUser!!.uid).removeValue()
    }
}