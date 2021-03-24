package com.io.unknow.parse

import com.google.firebase.database.DataSnapshot
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageDate
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText

object MessageParse {

    fun getMessageFromInterface(message: IMessage): IMessage? {
        return when (message) {
            is MessageText -> message
            is MessageImage -> message
            is MessageDate -> message
            else -> null
        }
    }

    fun getMessageFromShot(messageShot: DataSnapshot): IMessage{
        return if (messageShot.child("text").value != null){
            messageShot.getValue(MessageText::class.java)!!
        } else {
            messageShot.getValue(MessageImage::class.java)!!
        }
    }
}