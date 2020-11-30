package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.auth.TwoFieldModel
import com.io.unknow.livedata.TwoFieldLiveData

class TwoFieldViewModel: ViewModel() {
    val liveData = TwoFieldLiveData.get
    val model: TwoFieldModel = TwoFieldModel(liveData)
}