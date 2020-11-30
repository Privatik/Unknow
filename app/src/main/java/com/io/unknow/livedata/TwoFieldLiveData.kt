package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.Chat

class TwoFieldLiveData: LiveData<MutableMap<String, Chat>>() {
    companion object{
        val get = TwoFieldLiveData()
    }

    fun loadMessages(map: MutableMap<String ,Chat>){
        value = map
    }

    fun addMessages(userId: String, chat: Chat){
        value?.set(userId, chat)
    }
}