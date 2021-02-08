package com.io.unknow.firebase

import com.google.firebase.database.DatabaseReference
import com.io.unknow.app.App
import com.io.unknow.livedata.ChatListLiveData
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