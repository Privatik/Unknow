package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.Chat

class ChatListLiveData: LiveData<Map<String, Chat>>() {
    fun load(map: Map<String, Chat>){
        value = map
    }
}