package com.io.unknow.livedata

import androidx.lifecycle.LiveData
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.User

class ProfileLiveData: LiveData<User>() {

    fun setUser(user: User?) {
        value = user
        CurrentUser.user = user!!
    }
}