package com.io.unknow.model

import java.io.Serializable

data class Message(
    var text: String = "",
    var time: String = "",
    var userId: String = ""
):Serializable