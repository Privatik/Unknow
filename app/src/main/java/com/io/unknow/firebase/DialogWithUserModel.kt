package com.io.unknow.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.storage.StorageReference
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.app.App
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.parse.DataParse
import com.io.unknow.parse.MessageParse
import com.io.unknow.util.TAGS.MESSAGE
import com.io.unknow.util.TAGS.USER_ID
import com.io.unknow.util.UpdateDateToolbar
import java.io.File
import java.util.*
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
    @Inject lateinit var messaging: FirebaseMessaging

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

    private val keyMessages by lazy { mutableMapOf<String, IMessage>() }

    init {
        App.appComponent.inject(this)

        baseMessages = base.child(MESSAGES).child(messageId)
        liveData.load(mutableListOf())

        filesPhotos = storage.child(mAuth.currentUser!!.uid).child("photos")

        baseMy = base.child(CHATS).child(mAuth.currentUser!!.uid).child(userId)
        baseUser = base.child(CHATS).child(userId).child(mAuth.currentUser!!.uid)

        baseNotification = base.child("notification").child(userId).child(mAuth.currentUser!!.uid)
        base()
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
    }

    private fun base(){
        baseMessages.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               // Log.e("Message", "onChildAdded")
                val message = MessageParse.getMessageFromShot(snapshot)
                keyMessages[snapshot.key!!] = message!!

                if (isWritePermission) {
                    baseMy.child(LAST_MESSAGE).setValue(message)
                    baseUser.child(LAST_MESSAGE).setValue(message)
                }

                if (mAuth.currentUser!!.uid in message.listViewMessage) {
                    liveData.addMessage(UpdateDateToolbar.onChangeDate(message))
                    liveData.addMessage(message)
                }
                updateAllBase()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.e("Remove","${snapshot.key}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

            private fun updateAllBase() {
                val size = adapter.itemCount - 1
                if (size != -1) {
                    adapter.notifyDataSetChanged()
                    updateDialog.update(size)
                }
            }
        })
    }


    private fun postMessage(iMessage: IMessage){
        baseMessages.push().setValue(iMessage)
        if (!isReadUser){
            val message: RemoteMessage = if (iMessage is MessageText){
                RemoteMessage.Builder(userId)
                    .addData(USER_ID, iMessage.userId)
                    .addData(MESSAGE, iMessage.text)
                    .build()
            }else{
                RemoteMessage.Builder(userId)
                    .addData(USER_ID, iMessage.userId)
                    .addData(MESSAGE, "Фотография")
                    .build()
            }

            messaging.send(message)
        }
        if (!isWritePermission){
            isWritePermission = true
        }

        Log.e("Message","postMessage $iMessage")
    }

    fun createMessageText(message: String) {
        Log.e("Message","createMessageText $message")

        val messageModel = MessageText(text = message,time =  dataParse.getStringNow(),userId =  mAuth.currentUser!!.uid)

        messageModel.listViewMessage.add(userId)
        messageModel.listViewMessage.add(messageModel.userId)

        postMessage(messageModel)
    }


    fun createMessageImage(messageUrl: String) {

        val uuid = UUID.randomUUID()
        val file = filesPhotos.child("${uuid}")

        file.putFile(Uri.fromFile(File(messageUrl))).addOnSuccessListener {

            Log.i("CAMERA", "onSuc")
            file.downloadUrl.addOnCompleteListener {
                Log.i("CAMERA", "getDow")
                val messageModel = MessageImage(
                    imageUrl = it.result.toString(),
                    imagePlaceInBase = uuid.toString(),
                    time = dataParse.getStringNow(),
                    userId = mAuth.currentUser!!.uid
                )

                messageModel.listViewMessage.add(userId)
                messageModel.listViewMessage.add(messageModel.userId)

                postMessage(messageModel)
            }

        }.addOnFailureListener {
            Log.i("CAMERA", "${it.message}")
        }


    }

        fun changeDialog(isRead: Boolean) {
            baseMy.child(READ).setValue(isRead)
        }

        fun deleteMessage(message: IMessage, isDeleteForAll: Boolean) {
            if (message is MessageImage) {
                if(isDeleteForAll)
                    filesPhotos.child(message.imagePlaceInBase).delete()
            }

            val deleteMessage =
                baseMessages.child(keyMessages.filter { it.value == message }.keys.first())
            if (isDeleteForAll) {
                deleteMessage.removeValue()
                updateMessageField()
            } else {
                deleteMessage.child("listViewMessage")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            Log.i("Delete", error.message)
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (numberUser in snapshot.children) {
                                if (numberUser.value == mAuth.currentUser!!.uid) {
                                    deleteMessage.child("listViewMessage").child(numberUser.key!!)
                                        .removeValue()
                                    updateMessageField()
                                }
                            }
                        }
                    }
                    )
            }
        }

    fun updateMessage(messageText: MessageText) {
        val updateMessage =
            baseMessages.child(keyMessages.filter { it.value == messageText }.keys.first())

        messageText.isEdit = true
        updateMessage.setValue(messageText)
        updateMessageField()
    }

    private fun updateMessageField(){
        adapter.notifyDataSetChanged()
    }
}