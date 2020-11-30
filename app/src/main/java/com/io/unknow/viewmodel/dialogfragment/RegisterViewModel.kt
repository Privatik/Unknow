package com.io.unknow.viewmodel.dialogfragment

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    var login: String = ""
    var password: String = ""
    var passwordRepear: String = ""

    var isMenSex = 0

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        Log.i("viewModel","radio")
        isMenSex = id
        radioGroup.check(id)
    }
}