package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.firebase.ProfileModel
import com.io.unknow.model.User
import com.io.unknow.parse.DataParse
import com.io.unknow.util.Locate

class ProfileViewModel: ViewModel() {
    private val profileModel: ProfileModel = ProfileModel()
    var user: User? = null

    fun sexString():String{
        if (user?.sex == 0) return Locate.getSexMen()
        return Locate.getSexWoman()
    }

    fun ageString():String {
        return DataParse.getYear(user!!.age).toString() + Locate.getYear()
    }

    fun updateUserBase(){
        profileModel.update(user!!)
    }
}