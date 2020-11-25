package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.io.unknow.auth.ProfileModel
import com.io.unknow.livedata.ProfileLiveData
import com.io.unknow.model.User

class ProfileViewModel: ViewModel() {
    var liveData: ProfileLiveData = ProfileLiveData.get
    var profileModel: ProfileModel = ProfileModel(liveData)

}