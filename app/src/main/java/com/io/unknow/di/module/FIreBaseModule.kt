package com.io.unknow.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FIreBaseModule {

    @Provides
    @Singleton
    fun fireBaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun dataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    @Singleton
    fun store(): FirebaseFirestore = FirebaseFirestore.getInstance()
}