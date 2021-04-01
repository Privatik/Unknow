package com.io.unknow.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Chat(
    val messages: String = "",
    var last_message: IMessage? = null,
    var readNow: Boolean = false
): Parcelable