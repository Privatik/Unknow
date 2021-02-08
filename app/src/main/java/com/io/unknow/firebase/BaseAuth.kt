package com.io.unknow.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.io.unknow.app.App
import com.io.unknow.model.User
import com.io.unknow.parse.DataParse
import com.io.unknow.ui.activity.MainActivity
import javax.inject.Inject


class BaseAuth(val activity: Activity) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var databaseReference: DatabaseReference
    private var mAuthStateListener: FirebaseAuth.AuthStateListener

    init {
        App.appComponent.inject(this)
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser
            if (currentUser != null){
                Log.i("mAuth", "Connect ${currentUser.tenantId}")
            }
            else{
                Log.i("mAuth", "Disconnect")
            }
        }
    }

    fun createUser(email: String, password: String, user: User){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    user.id = task.result!!.user!!.uid
                    createDataBaseField(user, task.result!!.user!!.uid)
                    activity.startActivity(Intent(activity,MainActivity::class.java))
                    activity.finish()
                }
                else{
                    Log.i("Login","${task.exception}")
                    Toast.makeText(activity,"${task.exception}",Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun createDataBaseField(user: User,uid: String) {
       val ref = databaseReference.child("users").child(uid)
        if (DataParse.getYear(user.age) >= 18) {
            user.isBan = true
        }
        ref.setValue(user)
    }

    fun singIn(email: String, password: String){
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = mAuth.currentUser

                        databaseReference.child("users").child(currentUser!!.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    activity.startActivity(Intent(activity,MainActivity::class.java))
                                    activity.finish()
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                    } else {
                        Toast.makeText(activity, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun singOut() {
        mAuth.signOut()
    }

    fun addListener() {
        mAuth.addAuthStateListener(mAuthStateListener)
    }

    fun removeListener(){
        mAuth.removeAuthStateListener(mAuthStateListener)
    }
}