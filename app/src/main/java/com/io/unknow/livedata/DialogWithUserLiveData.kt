package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.MessageText

class DialogWithUserLiveData: LiveData<MutableList<MessageText>>() {
    fun load(list: MutableList<MessageText>){
        value = list
    }

    fun addMessage(messageText: MessageText){
        value?.add(messageText)
    }
}