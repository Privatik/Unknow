package com.io.unknow.livedata

import androidx.lifecycle.LiveData

class OnlineLiveData: LiveData<Boolean>() {

    fun setOnline(online: Boolean){
        value = online
    }
}