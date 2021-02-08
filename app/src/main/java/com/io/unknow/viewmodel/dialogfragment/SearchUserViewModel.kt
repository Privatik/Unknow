package com.io.unknow.viewmodel.dialogfragment

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.ViewModel
import com.io.unknow.firebase.SearchUserModel
import com.io.unknow.livedata.SearchUserLiveData
import com.io.unknow.model.Search
import com.io.unknow.model.SearchString


class SearchUserViewModel: ViewModel() {
    private val search: Search = Search()
    val searchString = SearchString()
    val liveData = SearchUserLiveData()
    private val searchUserModel = SearchUserModel(liveData)

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        Log.i("viewModel","radio")
        search.sex = id
        radioGroup.check(id)
    }

    fun find(){
        search.ageStart = if (searchString.ageStart == "") null else searchString.ageStart.toInt()
        search.ageEnd = if (searchString.ageEnd == "") null else searchString.ageEnd.toInt()
        search.heightStart = if (searchString.heightStart == "") null else searchString.heightStart.toInt()
        search.heightEnd = if (searchString.heightEnd == "") null else searchString.heightEnd.toInt()
        search.weightStart = if (searchString.weightStart == "") null else searchString.weightStart.toInt()
        search.weightEnd = if (searchString.weightEnd == "") null else searchString.weightEnd.toInt()
        searchUserModel.searchUser(search)
    }

    fun close() {
        searchUserModel.close()
    }
}