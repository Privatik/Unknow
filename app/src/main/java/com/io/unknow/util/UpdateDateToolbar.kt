package com.io.unknow.util

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.parse.CalendarParse

class UpdateDateToolbar(val mContext: Context, val adapter: DialogAdapter?,val layoutManager: LinearLayoutManager) {

    fun onScroll(date: String): String{
        var dateNew = date

        if (adapter != null) {
            val message = adapter.getMessage(layoutManager.findLastVisibleItemPosition())
            if (dateNew != message.time.substring(0,10)){
                ToastMessage.topMessage(mContext, message.time.substring(8, 10) + CalendarParse.getMounth(message.time.substring(5, 8)))
                dateNew = message.time.substring(0,10)
            }
        }
        Log.i("RecycleViw","Scroll down ${layoutManager.findFirstVisibleItemPosition()} ${layoutManager.findLastVisibleItemPosition()}")

        return dateNew
    }
}