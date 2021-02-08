package com.io.unknow.parse

class CheckParamentMode {

    fun mode(isStart: Boolean, isEnd: Boolean, start: Int?, end: Int?, value: Int?):Boolean{
        if (value == null && (isStart || isEnd)) return true

        if (isStart) {
            if (isEnd) {
                if (!check(start!!, end!!, value!!)) return true
            }else {
                if (checkStart(start!!, value!!)) return true
            }
        } else if (isEnd) {
            if (isStart) {
                if (!check(start!!, end!!, value!!)) return true
            }
            else {
                if (checkEnd(end!!, value!!)) return true
            }
        }

        return false
    }

    private fun check(start: Int, end : Int, value: Int): Boolean{
        return value in start..end
    }

    private fun checkStart(start: Int, value: Int): Boolean{
        return value > start
    }

    private fun checkEnd(end : Int, value: Int): Boolean{
        return value < end
    }
}