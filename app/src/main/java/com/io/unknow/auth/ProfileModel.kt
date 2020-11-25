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

class ProfileModel(liveData: ProfileLiveData) {

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var databaseReference: DatabaseReference

    init {
        App.appComponent.inject(this)
       val currentUser = mAuth.currentUser!!
        databaseReference.child("users").child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val user = snapshot.getValue(User::class.java)!!
                liveData.setUser(user)
            }

            override fun onCancelled(error: DatabaseError) {
               Log.i("User",error.message)
            }

        })
    }


}