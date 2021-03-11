package com.io.unknow.di.component

import com.io.unknow.firebase.*
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.di.module.FIreBaseModule
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.ui.activity.SplashScreen
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FIreBaseModule::class])
interface MainComponent {

    fun inject(baseAuth: BaseAuth)
    fun inject(profileModel: ProfileModel)
    fun inject(searchUserModel: SearchUserModel)
    fun inject(twoFieldModel: TwoFieldModel)
    fun inject(oneFieldModel: OneFieldModel)
    fun inject(chatListModel: ChatListModel)
    fun inject(dialogWithUserModel: DialogWithUserModel)
    fun inject(dialogActivityModel: DialogActivityModel)

    fun inject(activity: SplashScreen)

    fun inject(broadCast: NotificationListener)
}