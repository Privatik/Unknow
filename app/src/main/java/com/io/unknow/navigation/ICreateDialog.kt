package com.io.unknow.navigation

import com.io.unknow.model.Chat
import java.io.Serializable

interface ICreateDialog: Serializable {
    fun open(chat: Chat, userId: String)
}