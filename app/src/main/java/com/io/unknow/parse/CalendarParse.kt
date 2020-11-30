package com.io.unknow.parse

class CalendarParse {
    companion object{
        fun getMounth(mounth: String): String =
            when (mounth){
                "01" -> " Января"
                "02" -> " Февраля"
                "03" -> " Марта"
                "04" -> " Апреля"
                "05" -> " Мая"
                "06" -> " Июня"
                "07" -> " Июля"
                "08" -> " Августа"
                "09" -> " Сентября"
                "10" -> " Октября"
                "11" -> " Ноября"
                else -> " Декабря"
            }
    }
}