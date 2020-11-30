package com.io.unknow.parse

import android.util.Log
import androidx.core.util.TimeUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class DataParse {

    companion object{
        fun getString(date: Date):String{
            val df = SimpleDateFormat("yyyy-MM-dd")
            return df.format(date)
        }

        fun getYear(ageUser: String):Int{
            val now = Date()
            val ageUser = SimpleDateFormat("yyyy-MM-dd").parse(ageUser)!!

            return getAge(ageUser,now)
        }

        private fun getAge(myDate: Date,nowDate: Date):Int{
            var year = nowDate.year - myDate.year
            if (myDate.month > nowDate.month){
                year -= 1
            }
            else if (myDate.month == nowDate.month){
                if (nowDate.day < myDate.day){
                    year -= 1
                }
            }

            return year
        }
    }

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