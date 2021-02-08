package com.io.unknow.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.io.unknow.app.App
import com.io.unknow.model.User
import javax.inject.Inject

class ProfileModel {

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var databaseReference: DatabaseReference

    private val dataBase: DatabaseReference

    init {
        App.appComponent.inject(this)
       val currentUser = mAuth.currentUser!!
        dataBase = databaseReference.child("users").child(currentUser.uid)
    }

    fun update(user: User){
        dataBase.setValue(user)
    }


}