package com.io.unknow.di.component

import com.io.unknow.auth.*
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.di.module.MyFireBaseAuth
import com.io.unknow.di.module.MyFireBaseDatabase
import com.io.unknow.ui.activity.SplashScreen
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MyFireBaseAuth::class,MyFireBaseDatabase::class])
interface MainComponent {

    fun inject(baseAuth: BaseAuth)
    fun inject(profileModel: ProfileModel)
    fun inject(searchUserModel: SearchUserModel)
    fun inject(twoFieldModel: TwoFieldModel)
    fun inject(chatListModel: ChatListModel)
    fun inject(dialogWithUserModel: DialogWithUserModel)

    fun inject(activity: SplashScreen)

    fun inject(broadCast: NotificationListener)
}