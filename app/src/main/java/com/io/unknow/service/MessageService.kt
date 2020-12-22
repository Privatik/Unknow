package com.io.unknow.service

import android.app.IntentService
import android.content.Intent


class MessageService : IntentService("MessageSevice") {

    override fun onHandleIntent(intent: Intent?) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
