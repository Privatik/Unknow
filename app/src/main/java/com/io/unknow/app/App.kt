package com.io.unknow.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.io.unknow.di.component.DaggerMainComponent
import com.io.unknow.di.component.MainComponent

private const val SETTING = "setting"
class App: Application() {
    companion object{
        lateinit var appComponent: MainComponent
    }

    lateinit var sp: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initDagger()
        sp = getSharedPreferences(SETTING, Context.MODE_PRIVATE)
    }

    fun initDagger() {
         appComponent = DaggerMainComponent.builder().build()
    }
}