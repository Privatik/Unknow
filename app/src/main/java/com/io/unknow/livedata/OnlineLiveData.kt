package com.io.unknow.livedata

import androidx.lifecycle.LiveData

class OnlineLiveData: LiveData<Boolean>() {

    fun setOnline(online: Boolean?){
        if (online == null) {
            value = false
        } else{
            value = online
        }
    }
}