package com.io.unknow.viewmodel.dialogfragment

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.firebase.DialogWithUserModel
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.livedata.OnlineLiveData
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IUpdateDialog


class DialogWithUserViewModel : ViewModel() {
    val liveData = DialogWithUserLiveData()
    private lateinit var dialogWithUserModel: DialogWithUserModel

    fun loadMessages(chat: Chat, userId: String, updateDialog: IUpdateDialog){
        dialogWithUserModel = DialogWithUserModel(liveData, chat.messages, userId, updateDialog)
    }

    fun initAdapter(adapter: DialogAdapter){
        dialogWithUserModel.adapter = adapter
    }

    fun loadCallback(){
        dialogWithUserModel.inviteCallback()
    }

    fun sendMessageText(message: String){
        dialogWithUserModel.createMessageText(message)
    }

    fun sendMessagePhoto(messageUrl: String){
        dialogWithUserModel.createMessageImage(messageUrl)
    }

    fun changeDialog(isRead: Boolean){
        dialogWithUserModel.changeDialog(isRead)
    }

}