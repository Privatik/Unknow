package com.io.unknow.viewmodel.activity

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.io.unknow.firebase.BaseAuth
import com.io.unknow.model.User

class LoginViewModel: ViewModel() {

    private var baseAuth: BaseAuth? = null
    var loginT: String = ""
    var password: String = ""

    fun initBaseAuth(activity: Activity) {
        if (baseAuth == null) {
            baseAuth = BaseAuth(activity)
        }
    }

    fun addListener() {
        baseAuth?.addListener()
    }

    fun removeListener(){
        baseAuth?.removeListener()
    }

    fun createUser(email: String, password: String, user: User){
        baseAuth?.createUser(email, password, user)
    }

    fun singIn(context: Context){
        Log.i("Login","${loginT} ${password}")
        if (validateForm(loginT,password)) {
            baseAuth?.singIn(loginT, password)
        }
        else{
            Toast.makeText(context,"Empty",Toast.LENGTH_SHORT).show()
        }
    }

    fun singOut() {
        baseAuth?.singOut()
    }

    private fun validateForm(email: String,password: String): Boolean =
       email.isNotEmpty() && password.isNotEmpty()
}