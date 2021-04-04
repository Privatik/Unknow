package com.io.unknow.parse

import com.io.unknow.util.Locate

class CalendarParse {
    companion object{
        fun getMounth(mounth: String): String =
            when (mounth){
                "01" -> if (Locate.isRuLocate) " Января" else " January"
                "02" -> if (Locate.isRuLocate) " Февраля" else " February"
                "03" -> if (Locate.isRuLocate) " Марта" else " March"
                "04" -> if (Locate.isRuLocate) " Апреля" else " April"
                "05" -> if (Locate.isRuLocate) " Мая" else " May"
                "06" -> if (Locate.isRuLocate) " Июня" else " June"
                "07" -> if (Locate.isRuLocate) " Июля" else  " July"
                "08" -> if (Locate.isRuLocate) " Августа" else " August"
                "09" -> if (Locate.isRuLocate) " Сентября" else " September"
                "10" -> if (Locate.isRuLocate) " Октября" else " October"
                "11" -> if (Locate.isRuLocate) " Ноября" else " November"
                else -> if (Locate.isRuLocate) " Декабря" else " December"
            }
    }
}