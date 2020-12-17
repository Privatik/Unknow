package com.io.unknow.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast


object ToastMessage {
    private var toast: Toast? = null

    fun message(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun topMessage(context: Context, text: String){
        if (toast == null){
            toast = Toast.makeText(context, text, 1000)
            toast!!.setGravity(Gravity.TOP, 0, 0)
            toast!!.setMargin(0f, 0.1f)
            toast!!.show()
        }
        else if (!toast!!.view.isShown) {
            toast = Toast.makeText(context, text, 1000)
            toast!!.setGravity(Gravity.TOP, 0, 0)
            toast!!.setMargin(0f, 0.1f)
            toast!!.show()
        }
    }
}