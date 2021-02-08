package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.firebase.OneFieldModel
import com.io.unknow.livedata.OneFieldLiveData

class OneFieldViewModel: ViewModel() {
    val liveData: OneFieldLiveData = OneFieldLiveData()
    private val oneFieldModel = OneFieldModel(liveData)

    fun online(online: Boolean) = oneFieldModel.online(online)

    fun exit() {
        oneFieldModel.exit()
    }
}