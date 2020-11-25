package com.io.unknow.model

import java.io.Serializable

data class Chat(
    val messages: String = "",
    var last_message: Message? = null
):Serializable