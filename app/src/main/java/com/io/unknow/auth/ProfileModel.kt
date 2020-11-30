package com.io.unknow.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.ProfileLiveData
import com.io.unknow.model.User
import javax.inject.Inject

class ProfileModel(val liveData: ProfileLiveData) {

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var databaseReference: DatabaseReference

    private val dataBase: DatabaseReference

    init {
        App.appComponent.inject(this)
       val currentUser = mAuth.currentUser!!
        dataBase = databaseReference.child("users").child(currentUser.uid)
            dataBase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val userB = snapshot.getValue(User::class.java)!!
                liveData.setUser(userB)
            }

            override fun onCancelled(error: DatabaseError) {
               Log.i("User",error.message)
            }

        })
    }

    fun update(user: User){
        dataBase.setValue(user)
        liveData.setUser(user)
    }

    fun exit() {
        liveData.setUser(null)
        mAuth.signOut()
    }


}