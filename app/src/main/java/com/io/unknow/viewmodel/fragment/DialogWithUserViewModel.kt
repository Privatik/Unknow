package com.io.unknow.viewmodel.fragment

import androidx.lifecycle.ViewModel
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.firebase.DialogWithUserModel
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IUpdateDialog


class DialogWithUserViewModel : ViewModel() {
    val liveData = DialogWithUserLiveData()
    private var dialogWithUserModel: DialogWithUserModel? = null

    fun loadMessages(chat: Chat, userId: String, updateDialog: IUpdateDialog){
        if (dialogWithUserModel == null)
        dialogWithUserModel = DialogWithUserModel(liveData, chat.messages, userId, updateDialog)
    }

    fun initAdapter(adapter: DialogAdapter){

        dialogWithUserModel?.adapter = adapter
    }

    fun loadCallback(){
        dialogWithUserModel?.inviteCallback()
    }

    fun sendMessageText(message: String){
        dialogWithUserModel?.createMessageText(message)
    }

    fun sendMessagePhoto(messageUrl: String){
        dialogWithUserModel?.createMessageImage(messageUrl)
    }

    fun changeDialog(isRead: Boolean){
        dialogWithUserModel?.changeDialog(isRead)
    }

    fun deleteMessage(message: IMessage, isDeleteForAll: Boolean){
        dialogWithUserModel?.deleteMessage(message = message, isDeleteForAll = isDeleteForAll)
    }

    fun updateMessage(messageText: MessageText){
        dialogWithUserModel?.updateMessage(messageText = messageText)
    }

}