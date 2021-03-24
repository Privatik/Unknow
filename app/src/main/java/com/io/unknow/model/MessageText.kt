package com.io.unknow.model

import java.io.Serializable

data class MessageText(
    var text: String = "",
    override var time: String = "",
    override var userId: String = "",
    override var listViewMessage: MutableList<String> = mutableListOf(),
    override var isEdit: Boolean = false
): Serializable, IMessage