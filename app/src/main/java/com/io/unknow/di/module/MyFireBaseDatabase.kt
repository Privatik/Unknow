package com.io.unknow.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

@Module
class MyFireBaseDatabase {

   @Provides
   @Singleton
    fun dataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference
}