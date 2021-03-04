package com.io.unknow.viewmodel.activity

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel

class DialogViewModel: ViewModel() {

    var isPhotoLoad = false


    fun getCameraImages(context: Context): List<String> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val columnIndexData: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String?

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = context.contentResolver.query(uri, projection, null,
            null, null)

        columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }
}