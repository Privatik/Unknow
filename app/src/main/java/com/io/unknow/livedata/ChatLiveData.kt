package com.io.unknow.livedata

import androidx.lifecycle.LiveData

class ChatLiveData: LiveData<String>() {

    fun setMessage(str: String) {
        value = str
    }
}