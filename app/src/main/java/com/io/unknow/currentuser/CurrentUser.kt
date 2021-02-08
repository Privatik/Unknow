package com.io.unknow.currentuser

import com.io.unknow.model.Chat
import com.io.unknow.model.User

object CurrentUser {
    var user: User? = null
    lateinit var map: MutableMap<String, Chat>
}