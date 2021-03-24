package com.io.unknow.navigation

import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText

interface IDialogRedactMessage {

    fun edit (message: IMessage)

    fun delete (message: IMessage, isDeleteForAll: Boolean)

}