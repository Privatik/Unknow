package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.Message

class DialogWithUserLiveData: LiveData<MutableList<Message>>() {
    fun load(list: MutableList<Message>){
        value = list
    }

    fun addMessage(message: Message){
        value?.add(message)
    }
}