package com.io.unknow.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.app.App
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.parse.DataParse
import com.io.unknow.util.UpdateDateToolbar
import java.io.File
import javax.inject.Inject


private const val READ = "readNow"
private const val LAST_MESSAGE = "last_message"
private const val CHATS = "chats"
private const val MESSAGES = "messages"
private const val count = 50
class DialogWithUserModel(private val liveData: DialogWithUserLiveData, messageId: String,val userId: String,val updateDialog: IUpdateDialog) {

    @Inject lateinit var mAuth: FirebaseAuth
    @Inject lateinit var base: DatabaseReference
    @Inject lateinit var storage: StorageReference

    private var baseMessages: DatabaseReference
    private var isWritePermission = false
    private var isReadUser = false
    private var countMessages = 0
    private val baseNotification: DatabaseReference
    private val baseUser: DatabaseReference
    private val baseMy: DatabaseReference
    private val filesPhotos: StorageReference
    private val dataParse = DataParse()
    lateinit var adapter: DialogAdapter

    init {
        App.appComponent.inject(this)

        baseMessages = base.child(MESSAGES).child(messageId)
        liveData.load(mutableListOf())

        filesPhotos = storage.child(mAuth.currentUser!!.uid).child("photos")

        baseMy = base.child(CHATS).child(mAuth.currentUser!!.uid).child(userId)
        baseUser = base.child(CHATS).child(userId).child(mAuth.currentUser!!.uid)

        baseNotification = base.child("notification").child(userId).child(mAuth.currentUser!!.uid)
        inviteCallback()
        baseUser.child(READ).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isReadUser = snapshot.getValue(Boolean::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun inviteCallback(){
        if (isWritePermission) isWritePermission = false
        countMessages += count

        base()
    }

    private fun base(){
        baseMessages.limitToLast(countMessages).addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("AdapterDialog", "onChildAdded")
                val message = getMessage(snapshot)

                if (isWritePermission) {
                    baseMy.child(LAST_MESSAGE).setValue(message)
                    baseUser.child(LAST_MESSAGE).setValue(message)
                }
                liveData.addMessage(UpdateDateToolbar.onChangeDate(message))
                liveData.addMessage(message)
                updateAllBase()
            }

            private fun getMessage(messageShot: DataSnapshot): IMessage {
                return if (messageShot.child("text").value != null){
                    messageShot.getValue(MessageText::class.java)!!
                } else {
                    messageShot.getValue(MessageImage::class.java)!!
                }
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


    private fun postMessage(IMessage: IMessage){
        baseMessages.push().setValue(IMessage)
        if (!isReadUser){
            baseNotification.push().setValue(IMessage)
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

    fun createMessageImage(messageUrl: String){
        createMessageImage(Uri.fromFile(File(messageUrl)))
    }

    fun createMessageImage(messageUrl: Uri) {

        filesPhotos.putFile(messageUrl).addOnFailureListener {
            Log.i("CAMERA","${it.message}")
        }.addOnSuccessListener { taskSnapshot ->
            val downloadUrl = taskSnapshot.uploadSessionUri

            Log.i("CAMERA","onSuc")
            val messageModel = MessageImage(uri = downloadUrl.toString(),time =  dataParse.getStringNow(),userId =  mAuth.currentUser!!.uid)

            messageModel.listViewMessage.add(userId)
            messageModel.listViewMessage.add(messageModel.userId)

            postMessage(messageModel)
            }
    }

    fun changeDialog(isRead: Boolean){
        baseMy.child(READ).setValue(isRead)
    }

}