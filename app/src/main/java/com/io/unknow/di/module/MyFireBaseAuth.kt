package com.io.unknow.di.module

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyFireBaseAuth {

    @Provides
    @Singleton
    fun fireBaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
}