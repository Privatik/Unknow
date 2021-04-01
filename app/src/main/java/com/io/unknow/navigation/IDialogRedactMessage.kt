package com.io.unknow.navigation

import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText

interface IDialogRedactMessage<T> {

    fun edit (item: T)

    fun delete (item: T)
    fun delete (item: T, isDeleteForAll: Boolean)

}