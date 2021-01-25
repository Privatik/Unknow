package com.io.unknow.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.io.unknow.app.App
import com.io.unknow.model.Message
import com.io.unknow.model.NotificationMessage
import com.io.unknow.service.MessageService
import javax.inject.Inject

private const val NOTIFICATION = "notification"
private val TAG = NotificationListener::class.simpleName
class NotificationListener : BroadcastReceiver() {

    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var base: DatabaseReference


    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG!!,"Lived Broad")
        App.appComponent.inject(this)

        Log.i(TAG,"Lived Load")

        val list = mutableListOf<NotificationMessage>()
        base.child(NOTIFICATION).child(mAuth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userMessages in snapshot.children){
                    val notificationMessage = NotificationMessage(userMessages.key!!)
                    for (messageSnapshot in userMessages.children){
                        val message = messageSnapshot.getValue(Message::class.java)!!
                        notificationMessage.messageBigText += "${message.text} \n\n"
                        notificationMessage.lastMessage = message.text
                        //messageSnapshot.ref.removeValue()
                    }
                    notificationMessage.messageBigText = notificationMessage.messageBigText.trim()
                    list.add(notificationMessage)
                }

                if (list.isNotEmpty()) {
                    Log.i("Notification","Broad died ${list.size}")
                    context.startService(MessageService.newInstance(context, list))
                }
                Log.i(TAG,"Broad died ${list.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG,"Broad error")
            }
        })

        Log.i(TAG,"Lived Finish")
    }
}
