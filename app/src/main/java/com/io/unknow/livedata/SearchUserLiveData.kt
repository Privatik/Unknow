package com.io.unknow.livedata

import androidx.lifecycle.LiveData

class SearchUserLiveData: LiveData<Map<String, String>>() {

    fun founding(messageId: String, userId: String){
        val map = mutableMapOf<String, String>(userId to messageId)
        value = map
    }
}