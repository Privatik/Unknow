package com.io.unknow.util

import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageDate
import com.io.unknow.parse.CalendarParse

object UpdateDateToolbar {

    private var date: String = ""

    fun onChangeDate(IMessage: IMessage): MessageDate?{
        val currentDate = IMessage.time.substring(0,10)
        if (date != currentDate) {
            date = currentDate
            return MessageDate(time = "${currentDate.substring(8,10)} ${CalendarParse.getMounth(currentDate.substring(5,7))}")
        }
        return null
    }
}