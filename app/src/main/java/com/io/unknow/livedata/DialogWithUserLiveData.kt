package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.IMessage

class DialogWithUserLiveData: LiveData<MutableList<IMessage>>() {
    fun load(list: MutableList<IMessage>){
        value = list
    }

    fun addMessage(iMessage: IMessage?){
        if (iMessage != null) {
            value?.add(iMessage)
        }
    }
}