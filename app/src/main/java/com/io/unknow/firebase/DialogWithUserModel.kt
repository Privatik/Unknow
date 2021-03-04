package com.io.unknow.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.app.App
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.livedata.OnlineLiveData
import com.io.unknow.model.Message
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.parse.DataParse
import javax.inject.Inject

private const val READ = "readNow"
private const val LAST_MESSAGE = "last_message"
private const val CHATS = "chats"
private const val MESSAGES = "messages"
private const val count = 50
class DialogWithUserModel(private val liveData: DialogWithUserLiveData,val liveDataOnline: OnlineLiveData, messageId: String,val userId: String,val updateDialog: IUpdateDialog) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference

    private lateinit var baseMessages: DatabaseReference
    private var isWritePermission = false
    private var isReadUser = false
    private var countMessages = 0
    private val baseNotification: DatabaseReference
    private val baseUser: DatabaseReference
    private val baseMy: DatabaseReference
    private val dataParse = DataParse()
    lateinit var adapter: DialogAdapter

    init {
        App.appComponent.inject(this)

        onlineUser()

        baseMessages = base.child(MESSAGES).child(messageId)
        liveData.load(mutableListOf())

        baseMy = base.child(CHATS).child(mAuth.currentUser!!.uid).child(userId)
        baseUser = base.child(CHATS).child(userId).child(mAuth.currentUser!!.uid)
        baseNotification = base.child("notification").child(userId).child(mAuth.currentUser!!.uid)
        InviteCallback()
        base()
        baseUser.child(READ).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isReadUser = snapshot.getValue(Boolean::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun InviteCallback(){
        if (isWritePermission) isWritePermission = false
        countMessages += count
    }

    private fun base(){
        baseMessages.limitToLast(countMessages).addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("AdapterDialog", "onChildAdded")
                val message = snapshot.getValue(MessageText::class.java)!!
                liveData.addMessage(message)
                if (isWritePermission) {
                    baseMy.child(LAST_MESSAGE).setValue(message)
                    baseUser.child(LAST_MESSAGE).setValue(message)
                }
                updateAllBase()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

            private fun updateAllBase() {
                adapter.notifyDataSetChanged()
                updateDialog.update(adapter.itemCount - 1)
            }
        })
    }


    private fun postMessage(message: Message){
        baseMessages.push().setValue(message)
        if (!isReadUser){
            baseNotification.push().setValue(message)
        }
        if (!isWritePermission){
            isWritePermission = true
        }
    }

    fun createMessageText(message: String) {
        val messageModel = MessageText(text = message,time =  dataParse.getStringNow(),userId =  mAuth.currentUser!!.uid)
        messageModel.listViewMessage.add(userId)
        messageModel.listViewMessage.add(messageModel.userId)

        postMessage(messageModel)
    }

    fun createMessageImage(messageUrl: String) {
        val messageModel = MessageImage(imageUrl = messageUrl,time =  dataParse.getStringNow(),userId =  mAuth.currentUser!!.uid)
        messageModel.listViewMessage.add(userId)
        messageModel.listViewMessage.add(messageModel.userId)

        postMessage(messageModel)
    }

    fun changeDialog(isRead: Boolean){
        baseMy.child(READ).setValue(isRead)
    }

   /* private fun getChat(userId: String){
        base.child(CHATS).child(mAuth.currentUser!!.uid).child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chat = snapshot.getValue(Chat::class.java)!!
                baseMessages = base.child(MESSAGES).child(chat.messages)
                liveData.load(mutableListOf())
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }*/

    fun onlineUser(){
        base.child("online").child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                liveDataOnline.setOnline(snapshot.getValue(Boolean::class.java))
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}