package com.io.unknow.viewmodel.dialogfragment

import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    var login: String = ""
    var password: String = ""
    var passwordRepea: String = ""

    var isMenSex = true

    fun sex():String{
        return if (isMenSex) "Men"
        else "Women"
    }
}