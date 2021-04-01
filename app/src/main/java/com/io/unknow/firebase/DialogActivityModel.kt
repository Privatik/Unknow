package com.io.unknow.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.OnlineLiveData
import com.io.unknow.model.Chat
import com.io.unknow.parse.MessageParse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CHATS = "chats"
class DialogActivityModel(private val liveDataOnline: OnlineLiveData) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference

    init {
        App.appComponent.inject(this)
    }

    @ExperimentalCoroutinesApi
    fun getChat(userId: String) : Flow<Chat> = channelFlow {
        base.child(CHATS).child(mAuth.currentUser!!.uid).child(userId)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chat = Chat(messages = snapshot.child("messages").getValue(String::class.java)!!,
                    last_message = MessageParse.getMessageFromShot(snapshot.child("last_message")),
                    readNow = snapshot.child("readNow").getValue(Boolean::class.java)!!)
                    Log.e("Load","${chat}")
                    launch { send(element = chat) }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun onlineUser(userId: String){
        base.child("online").child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                liveDataOnline.setOnline(snapshot.getValue(Boolean::class.java))
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}