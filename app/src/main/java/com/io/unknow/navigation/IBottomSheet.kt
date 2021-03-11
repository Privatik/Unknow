package com.io.unknow.navigation

interface IBottomSheet {
    fun loadBottomSheet()
    fun closeBottomSheet()

    fun loadInterfaceForLoadImages(loadImageFromGallery: ILoadImageFromGallery)
}