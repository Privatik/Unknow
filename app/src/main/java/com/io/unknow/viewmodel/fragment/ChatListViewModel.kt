package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.firebase.ChatListModel
import com.io.unknow.livedata.ChatListLiveData

class ChatListViewModel: ViewModel() {
    val model: ChatListModel = ChatListModel()

    fun deleteChat(userId: String){
        model.deleteChat(userId = userId)
    }
}