package com.io.unknow.viewmodel.dialogfragment

import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.ViewModel
import com.io.unknow.auth.CreateChatModel
import com.io.unknow.auth.SearchUserModel
import com.io.unknow.livedata.SearchUserLiveData
import com.io.unknow.model.Search


class SearchUserViewModel: ViewModel() {
    val search: Search = Search()
    val liveData = SearchUserLiveData()
    private val searchUserModel = SearchUserModel(liveData)

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        Log.i("viewModel","radio")
        search.sex = id
        radioGroup.check(id)
    }

    fun find(){
        searchUserModel.searchUser(search)
    }

    fun close() {
        searchUserModel.close()
    }
}