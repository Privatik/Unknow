package com.io.unknow.service

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.io.unknow.util.TAGS.ID
import com.io.unknow.util.TAGS.MESSAGE
import com.io.unknow.util.TAGS.USER_ID


private const val CHANEL_ID = "Notification"
private val TAG = MessageService::class.simpleName
class MessageService : FirebaseMessagingService(){

    private var count = 1000

    companion object{
        const val INTENT_SEND_MESSAGE = "Intent send message app"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val userId = remoteMessage.data[USER_ID]!!
        val message = remoteMessage.data[MESSAGE]!!

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
            action = INTENT_SEND_MESSAGE
            putExtra(USER_ID,userId)
            putExtra(MESSAGE,message)
            putExtra(ID,count)
        })

        count++

        Log.e(CHANEL_ID,"Received $message")
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)

        Log.e(CHANEL_ID,"Sent $p0")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

        Log.e(CHANEL_ID,"onDeleted")
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.e(CHANEL_ID,p0)
    }
}
