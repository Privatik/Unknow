package com.io.unknow.navigation

import android.widget.Button
import android.widget.ImageButton

interface IBottomSheet {
    fun loadBottomSheet()
    fun closeBottomSheet()

    fun loadInterfaceForLoadImages(loadImageFromGallery: ILoadImageFromGallery, sendButton: ImageButton)
}