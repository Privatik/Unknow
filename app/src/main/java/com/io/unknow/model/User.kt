package com.io.unknow.model

import java.io.Serializable

data class User(
    var id: String = "",
    var sex: Int = 0,
    var age: String = "",
    var height: Int? = null,
    var weight: Int? = null,
    var isBan: Boolean = false,
    var local: String? = null) : Serializable