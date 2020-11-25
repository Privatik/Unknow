package com.io.unknow.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.app.App
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.model.Message
import com.io.unknow.parse.DataParse
import javax.inject.Inject

class DialogWithUserModel(private val liveData: DialogWithUserLiveData, messageId: String) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference
    private val baseCurrent: DatabaseReference
    private val dataParse = DataParse()
    lateinit var adapter: DialogAdapter

    init {
        App.appComponent.inject(this)

        baseCurrent = base.child("messages").child(messageId)
        liveData.load(mutableListOf())
        /*baseCurrent.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    Log.i("Message", messageId)
                    Log.i("Message","${snapshot.key}")
                    for (message in snapshot.children){
                        Log.i("Message","${message.key}")
                        list.add(message.getValue(Message::class.java)!!)
                    }

                    liveData.load(list)
                }
                override fun onCancelled(error: DatabaseError) {

                }

            }
        )*/
    }

    fun InviteCallback(){
        baseCurrent.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("AdapterDialog", "onChildAdded")
                liveData.addMessage(snapshot.getValue(Message::class.java)!!)
                updateAllBase()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            private fun updateAllBase() {
                adapter.notifyDataSetChanged()
            }
        })
    }


    private fun postMessage(message: Message){
        baseCurrent.push().setValue(message)
    }

    fun createMessage(message: String) {
        val messageModel = Message(message, dataParse.getStringNow(), mAuth.currentUser!!.uid)
        postMessage(messageModel)
    }
}