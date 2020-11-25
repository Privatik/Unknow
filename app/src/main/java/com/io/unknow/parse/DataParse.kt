package com.io.unknow.parse

import java.text.SimpleDateFormat
import java.util.*

class DataParse {

    fun getData(date: String):Date{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
    }

    fun getStringNow():String{
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(Date())
    }

    fun getString(date: Date):String{
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(date)
    }

}