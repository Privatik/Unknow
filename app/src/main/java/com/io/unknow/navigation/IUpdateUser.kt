package com.io.unknow.navigation

import java.io.Serializable

interface IUpdateUser: Serializable {
    fun update(height: String,weight: String)
}