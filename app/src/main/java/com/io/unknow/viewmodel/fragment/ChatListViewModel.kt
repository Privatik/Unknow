package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.firebase.ChatListModel
import com.io.unknow.livedata.ChatListLiveData

class ChatListViewModel: ViewModel() {
    val liveData: ChatListLiveData = ChatListLiveData()
    val model: ChatListModel = ChatListModel()
}