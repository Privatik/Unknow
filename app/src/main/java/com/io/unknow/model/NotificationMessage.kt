package com.io.unknow.model

import java.io.Serializable

data class NotificationMessage(
    val userId: String,
    var messageBigText: String = "",
    var lastMessage: String = ""
): Serializable