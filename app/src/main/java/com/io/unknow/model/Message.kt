package com.io.unknow.model

import java.io.Serializable

data class Message(
    var text: String = "",
    var imageUrl: String = "",
    var time: String = "",
    var userId: String = "",
    var listViewMessage: MutableList<String> = mutableListOf(),
    var type: TypeMessage = TypeMessage.TEXT
): Serializable