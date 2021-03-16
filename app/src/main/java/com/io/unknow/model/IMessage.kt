package com.io.unknow.model

import java.io.Serializable

interface IMessage : Serializable {
    var time: String
    var userId: String
    var listViewMessage: MutableList<String>
}