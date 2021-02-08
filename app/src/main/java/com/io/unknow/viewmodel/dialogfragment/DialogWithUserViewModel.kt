package com.io.unknow.viewmodel.dialogfragment

import androidx.lifecycle.ViewModel
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.firebase.DialogWithUserModel
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.livedata.OnlineLiveData
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IUpdateDialog

class DialogWithUserViewModel : ViewModel() {
    val liveData = DialogWithUserLiveData()
    val liveDataOnline = OnlineLiveData()
    private lateinit var dialogWithUserModel: DialogWithUserModel

    fun loadMessages(chat: Chat?, userId: String, updateDialog: IUpdateDialog){
        dialogWithUserModel = if (chat != null){
            DialogWithUserModel(liveData, liveDataOnline,chat.messages, userId, updateDialog)
        } else{
            DialogWithUserModel(liveData, liveDataOnline,"", userId, updateDialog)
        }
    }

    fun initAdapter(adapter: DialogAdapter){
        dialogWithUserModel.adapter = adapter
    }

    fun loadCallback(){
        dialogWithUserModel.InviteCallback()
    }

    fun sendMessage(message: String){
        dialogWithUserModel.createMessage(message)
    }

    fun changeDialog(isRead: Boolean){
        dialogWithUserModel.changeDialog(isRead)
    }

}