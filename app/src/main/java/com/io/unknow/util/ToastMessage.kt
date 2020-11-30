package com.io.unknow.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast


object ToastMessage {
    fun message(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun topMessage(context: Context, text: String){
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.setMargin(0f,0.1f)
        toast.show()
    }
}