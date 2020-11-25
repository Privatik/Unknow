package com.io.unknow.navigation

import com.io.unknow.model.User

interface ICreateUser {
    fun createAccount(email: String, password: String, user: User)
}