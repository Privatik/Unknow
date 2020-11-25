package com.io.unknow.model

import java.util.*

data class SearchMen(
   val search: Search,
   val user: User,
   val uuid: String = UUID.randomUUID().toString(),
   var uidUserFriend: String? = null
)