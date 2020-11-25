package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.Chat

class TwoFieldLiveData: LiveData<Map<String, Chat>>() {
    fun loadMessages(map: Map<String ,Chat>){
        value = map
    }
}