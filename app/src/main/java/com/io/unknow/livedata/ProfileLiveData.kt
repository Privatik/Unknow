package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.model.User

class ProfileLiveData: LiveData<User>() {

    companion object{
         val get:ProfileLiveData = ProfileLiveData()
    }

    fun setUser(user: User) {
        value = user
    }
}