package com.io.unknow.model

import java.io.Serializable

data class User(
    var id: String = "",
    val sex: String = "",
    val age: Int = 0,
    var height: Int? = null,
    var weight: Int? = null,
    var local: String? = null) : Serializable