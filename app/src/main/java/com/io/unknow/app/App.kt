package com.io.unknow.app

import android.app.Application
import com.io.unknow.di.component.DaggerMainComponent
import com.io.unknow.di.component.MainComponent

class App: Application() {
    companion object{
        lateinit var appComponent: MainComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
         appComponent = DaggerMainComponent.builder().build()
    }
}