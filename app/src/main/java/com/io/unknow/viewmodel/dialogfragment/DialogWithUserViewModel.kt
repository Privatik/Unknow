package com.io.unknow.viewmodel.dialogfragment

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.auth.DialogWithUserModel
import com.io.unknow.livedata.DialogWithUserLiveData

class DialogWithUserViewModel : ViewModel() {
    val liveData = DialogWithUserLiveData()
    private lateinit var dialogWithUserModel: DialogWithUserModel

    fun loadMessages(messageId: String, userId: String){
        dialogWithUserModel = DialogWithUserModel(liveData,messageId, userId)
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

}