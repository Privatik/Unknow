package com.io.unknow.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.livedata.TwoFieldLiveData
import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
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

                   val chat = Chat(messages = value.child("messages").getValue(String::class.java)!!,
                       last_message = getMessage(value.child("last_message")),
                       readNow = value.child("readNow").getValue(Boolean::class.java)!!)

                    Log.i("Message","This is id messages $chat")

                    map[value.key!!] = chat
                }
                liveData.loadMessages(map)
            }

            private fun getMessage(messageShot: DataSnapshot): IMessage {
                return if (messageShot.child("text").value != null){
                    messageShot.getValue(MessageText::class.java)!!
                } else {
                    messageShot.getValue(MessageImage::class.java)!!
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}