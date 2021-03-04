package com.io.unknow.model

import java.io.Serializable

data class MessageImage ( var imageUrl: String = "",
                          var time: String = "",
                          var userId: String = "",
                          var listViewMessage: MutableList<String> = mutableListOf(),
                          val type: TypeMessage = TypeMessage.IMAGE
): Serializable, Message