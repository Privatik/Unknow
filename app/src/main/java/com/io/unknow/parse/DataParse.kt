package com.io.unknow.parse

import java.text.SimpleDateFormat
import java.util.*

class DataParse {

    companion object{
        private val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val simpleAllDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

        fun getString(date: Date):String{
            val df = simpleAllDate
            return df.format(date)
        }

        fun getYear(ageUser: String):Int{
            val now = Date()
            return getAge(simpleDate.parse(ageUser)!!,now)
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
        return simpleAllDate.parse(date)
    }

    fun getStringNow():String{
        val df = simpleAllDate
        return df.format(Date())
    }

    fun getString(date: Date):String{
        val df = simpleAllDate
        return df.format(date)
    }

}