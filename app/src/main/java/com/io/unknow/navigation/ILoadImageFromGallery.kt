package com.io.unknow.navigation

interface ILoadImageFromGallery {
    fun sendImages(imagesList: List<String>)
    fun sendImage(imageUrl: String)

    fun isChangeBooleanSend(isChange: Boolean)
}