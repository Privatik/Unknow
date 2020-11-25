package com.io.unknow.viewmodel.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.io.unknow.auth.BaseAuth
import com.io.unknow.model.User
import com.io.unknow.ui.activity.MainActivity

class LoginViewModel: ViewModel() {

    private var baseAuth: BaseAuth? = null
    var login: String = ""
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

    fun singIn(context: Context, email: String, password: String){
        if (validateForm(email,password)) {
            baseAuth?.singIn(email, password)
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