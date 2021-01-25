package com.io.unknow.viewmodel.dialogfragment

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.auth.DialogWithUserModel
import com.io.unknow.livedata.DialogWithUserLiveData
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IUpdateDialog

class DialogWithUserViewModel : ViewModel() {
    val liveData = DialogWithUserLiveData()
    private lateinit var dialogWithUserModel: DialogWithUserModel

    fun loadMessages(chat: Chat?, userId: String, updateDialog: IUpdateDialog){
        if (chat != null){
            dialogWithUserModel = DialogWithUserModel(liveData,chat.messages, userId, updateDialog)
        }
        else{
            dialogWithUserModel = DialogWithUserModel(liveData,"", userId, updateDialog)
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