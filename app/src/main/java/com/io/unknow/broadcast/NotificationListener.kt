package com.io.unknow.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.io.unknow.R
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.util.TAGS.ID
import com.io.unknow.util.TAGS.MESSAGE
import com.io.unknow.util.TAGS.NOTIFICATION
import com.io.unknow.util.TAGS.USER_ID
import com.kirich1409.androidnotificationdsl.notification

private val TAG = NotificationListener::class.simpleName
class NotificationListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val userId = intent.getStringExtra(USER_ID)!!

        val manager = NotificationManagerCompat.from(context)
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context,
            DialogActivity::class.java).putExtra(USER_ID, userId),
            0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION,
                "My Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.setSound(null, null)
            manager.createNotificationChannel(notificationChannel)
        }

        val builder = notification(context, NOTIFICATION, smallIcon = R.mipmap.icon) {
            contentTitle(userId)
            contentText(intent.getStringExtra(MESSAGE)!!)
            contentIntent(pendingIntent)
            priority(NotificationCompat.PRIORITY_DEFAULT)
        }

        manager.notify(intent.getIntExtra(ID,1111), builder)

    }
}
