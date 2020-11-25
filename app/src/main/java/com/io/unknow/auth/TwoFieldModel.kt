package com.io.unknow.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.TwoFieldLiveData
import com.io.unknow.model.Chat
import com.io.unknow.model.Message
import javax.inject.Inject

class TwoFieldModel(liveData: TwoFieldLiveData) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var dataBase: DatabaseReference

    init {
        App.appComponent.inject(this)

        val currentUser = mAuth.currentUser!!
        dataBase.child("chats").child(currentUser.uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = mutableMapOf<String, Chat>()
                for (value in snapshot.children){
                    map[value.key!!] = value.getValue(Chat::class.java)!!

                    Log.i("Message","This is id messages ${map[value.key!!]!!.messages} ${map[value.key!!]!!.last_message}")
                }
               liveData.loadMessages(map)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}