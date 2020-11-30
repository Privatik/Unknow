package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.io.unknow.auth.ProfileModel
import com.io.unknow.livedata.ProfileLiveData
import com.io.unknow.model.User
import com.io.unknow.parse.DataParse

class ProfileViewModel: ViewModel() {
    var liveData: ProfileLiveData = ProfileLiveData.get
    private val profileModel: ProfileModel = ProfileModel(liveData)
    var user: User = User()

    fun updateUser(){
        user = liveData.value!!
    }

    fun sexString():String{
        if (user.sex == 0) return "Муж"
        return "Жен"
    }

    fun ageString():String {
        return DataParse.getYear(user.age).toString() + " лет"
    }

    fun updateUserBase(){
        profileModel.update(user)
    }

    fun exit(){
        profileModel.exit()
    }
}