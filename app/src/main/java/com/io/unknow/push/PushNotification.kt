package com.io.unknow.push

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.io.unknow.broadcast.NotificationListener

class PushNotification(val activity: AppCompatActivity) {

    private var pendingIntent: PendingIntent = PendingIntent.getBroadcast(activity.baseContext, 0, Intent(activity.baseContext, NotificationListener::class.java), 0)

    fun createAlarm() {
        val am = activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        am.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            30000,
            pendingIntent
        )

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            am.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                30000,
                pendingIntent
            )
        } else {
            am.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                30000,
                pendingIntent
            )
        }
    }

    fun cancelAlarm() {
        val am = activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
    }
}