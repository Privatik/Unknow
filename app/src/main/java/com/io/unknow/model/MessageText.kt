package com.io.unknow.model

import java.io.Serializable

data class MessageText(
    var text: String = "",
    var time: String = "",
    var userId: String = "",
    var listViewMessage: MutableList<String> = mutableListOf(),
    val type: TypeMessage = TypeMessage.TEXT
): Serializable, Message