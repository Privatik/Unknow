package com.io.unknow.util

object Locate {
    private var isRuLocate = true

    fun initLocate(){
        isRuLocate = Setting.getLocateLanguage() == "ru"
    }

    fun getYear(): String = if (isRuLocate) " лет" else " years"


    fun getSexMen(): String = if (isRuLocate) "Муж" else "Men"
    fun getSexWoman(): String = if (isRuLocate) "Жен" else "Wom"

    fun getWeight(): String = if (isRuLocate) " кг" else " kg"

    fun getHeight(): String = if (isRuLocate) " см" else " cm"


}