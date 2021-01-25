package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.Chat

class TwoFieldLiveData: LiveData<MutableMap<String, Chat>>() {

    fun loadMessages(map: MutableMap<String ,Chat>){
      value = map
        CurrentUser.map = map
    }
}