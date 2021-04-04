package com.io.unknow.push

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.service.MessageService.Companion.INTENT_SEND_MESSAGE

class PushNotification(val activity: AppCompatActivity) {

    private val broadcast = NotificationListener()

    fun register() {
        val filter = IntentFilter(INTENT_SEND_MESSAGE)
        LocalBroadcastManager.getInstance(activity.baseContext).registerReceiver(broadcast, filter)
    }

    fun unregister() {
        LocalBroadcastManager.getInstance(activity.baseContext).unregisterReceiver(broadcast)
    }
}