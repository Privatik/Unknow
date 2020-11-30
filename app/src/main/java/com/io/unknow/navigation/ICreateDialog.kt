package com.io.unknow.navigation

import java.io.Serializable

interface ICreateDialog: Serializable {
    fun open(messageId: String, userId: String)
}