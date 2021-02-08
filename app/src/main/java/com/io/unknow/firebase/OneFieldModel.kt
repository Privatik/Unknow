package com.io.unknow.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.OneFieldLiveData
import com.io.unknow.model.User
import javax.inject.Inject

class OneFieldModel(val liveData: OneFieldLiveData) {

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var databaseReference: DatabaseReference

    private val dataBase: DatabaseReference
    private val dataBaseOnline: DatabaseReference


    init {
        App.appComponent.inject(this)
        val currentUser = mAuth.currentUser!!
        dataBase = databaseReference.child("users").child(currentUser.uid)
        dataBaseOnline = databaseReference.child("online").child(currentUser.uid)
        dataBase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userB = snapshot.getValue(User::class.java)!!
                liveData.setUser(userB)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("User",error.message)
            }

        })
    }

    fun online(online: Boolean){
       dataBaseOnline.setValue(online)
    }

    fun exit() {
        mAuth.signOut()
    }
}