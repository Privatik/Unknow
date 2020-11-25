package com.io.unknow.util

import android.content.Context
import android.widget.Toast

object ToastMessage {
    fun message(context: Context, text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
}