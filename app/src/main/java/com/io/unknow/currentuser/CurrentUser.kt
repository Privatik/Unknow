package com.io.unknow.currentuser

import com.io.unknow.model.Chat
import com.io.unknow.model.User

object CurrentUser {
    lateinit var user: User
    lateinit var map: MutableMap<String, Chat>
}