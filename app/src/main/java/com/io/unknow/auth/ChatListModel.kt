package com.io.unknow.auth

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.ChatListLiveData
import com.io.unknow.livedata.TwoFieldLiveData
import com.io.unknow.model.Chat
import javax.inject.Inject

class ChatListModel(liveData: ChatListLiveData) {
    @Inject
    lateinit var dataBase: DatabaseReference

    init {
        App.appComponent.inject(this)

        val map = mutableMapOf<String, Chat>()
    }
}