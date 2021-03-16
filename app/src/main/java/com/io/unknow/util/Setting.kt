package com.io.unknow.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.io.unknow.app.App
import java.util.*

private const val LANGUAGE = "language"
private const val THEME = "theme"
object Setting {
    private lateinit var sp: SharedPreferences

    fun initActivity(mContext: Context){
        sp = (mContext.applicationContext as App).sp
        updateLanguage(mContext)
        updateTheme(getThemeBlack())
        Locate.initLocate()
    }

    fun edit(): SharedPreferences.Editor{
        return sp.edit()
    }

    fun setLanguage(edit: SharedPreferences.Editor, language: String){
        edit.putString(LANGUAGE,language)
        edit.apply()
    }

    fun setTheme(edit: SharedPreferences.Editor, isBlack: Boolean){
        edit.putBoolean(THEME,isBlack)
        edit.apply()
    }

    fun getThemeBlack():Boolean{
       return sp.getBoolean(THEME,true)
    }

    fun updateLanguage(mContext: Context){
        val language = getLocateLanguage()

            Log.i("Setting",language)
          //  if (Locale.getDefault().language == language) return

            val myLocale = Locale(language)

            Locale.setDefault(myLocale)
            val config = Configuration()
            config.setLocale(myLocale)
            mContext.resources.updateConfiguration(config, mContext.resources.displayMetrics)
    }

    fun updateTheme(isBlack: Boolean) {
        if (isBlack) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getLocateLanguage():String =
        sp.getString(LANGUAGE, Locale.getDefault().language.toLowerCase(Locale.getDefault()))
            .toString()
}