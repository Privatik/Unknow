package com.io.unknow.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class MessageDate(
    override var time: String = "",
    override var userId: String = "",
    override var listViewMessage: MutableList<String> = mutableListOf(),
    override var isEdit: Boolean = false
): Parcelable, IMessage