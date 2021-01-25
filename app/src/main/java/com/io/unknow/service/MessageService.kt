package com.io.unknow.service

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.io.unknow.R
import com.io.unknow.model.Message
import com.io.unknow.model.NotificationMessage
import com.io.unknow.ui.dialogfragment.DialogWithUserFragment
import java.io.Serializable


private const val CHANEL_ID = "Notification"
private val TAG = MessageService::class.simpleName
class MessageService : IntentService("MessageService") {

    companion object{
        private const val LIST_MESSAGE = "list_message"
        private var count = 1

        fun newInstance(context: Context, list: MutableList<NotificationMessage>): Intent{
            val intent = Intent(context, MessageService::class.java)
            intent.putExtra(LIST_MESSAGE, list as Serializable)
            return intent
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i("Notification", "onHandle")
        val list = intent?.getSerializableExtra(LIST_MESSAGE) as MutableList<NotificationMessage>
        Log.i(TAG!!, "onHandle ${list.size}")
        Log.i("Notification", "onHandle ${list.size}")
        createNotification(list)
    }

    private fun createNotification(list: MutableList<NotificationMessage>){
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        for (message in list) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                DialogWithUserFragment.newInstance(baseContext, message.userId),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    CHANEL_ID,
                    "My Notifications",
                    NotificationManager.IMPORTANCE_LOW
                )
                notificationChannel.setSound(null, null)
                manager.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, CHANEL_ID)
            builder.setSmallIcon(R.mipmap.icon)
                .setContentTitle(message.userId)
                .setContentText(message.lastMessage)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.messageBigText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)

            Log.i(TAG!!, "Notification create")
            manager.notify(count, builder.build())
        }

    }
}
