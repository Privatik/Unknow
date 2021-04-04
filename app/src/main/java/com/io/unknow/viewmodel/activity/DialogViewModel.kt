package com.io.unknow.viewmodel.activity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.io.unknow.adapter.AdapterGallery
import com.io.unknow.firebase.DialogActivityModel
import com.io.unknow.livedata.OnlineLiveData
import com.io.unknow.model.Chat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DialogViewModel: ViewModel() {

    var isPhotoLoad = false
    val liveDataOnline = OnlineLiveData()
    private val dialogActivityModel = DialogActivityModel(liveDataOnline)
    private var listImage: MutableList<String> = mutableListOf()

    val sparedFlow = MutableSharedFlow<String>()

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("Recycle")
    fun getCameraImages(context: Context): List<String> {
        if (listImage.isEmpty()) {
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor?
            val columnIndexData: Int
            val listOfAllImages = ArrayList<String>()
            var absolutePathOfImage: String?

            val projection =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            cursor = context.contentResolver.query(
                uri, projection, null,
                null, null
            )

            columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(columnIndexData)
                listOfAllImages.add(absolutePathOfImage)
            }
            listImage = listOfAllImages
            return listOfAllImages
        }else
            return listImage
    }

    @ExperimentalCoroutinesApi
    fun getChat(userId: String): Flow<Chat>  = dialogActivityModel.getChat(userId = userId)

    fun online(userId: String){ dialogActivityModel.onlineUser(userId = userId) }
}