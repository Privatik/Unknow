package com.io.unknow.model

import android.os.Parcelable
import java.io.Serializable

interface IMessage : Parcelable {
    var time: String
    var userId: String
    var listViewMessage: MutableList<String>
    var isEdit: Boolean
}