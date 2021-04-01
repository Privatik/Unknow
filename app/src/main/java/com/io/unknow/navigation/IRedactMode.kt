package com.io.unknow.navigation

import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage

interface IRedactModeActivity {

    fun redactModeOn()
    fun redactModeOff()

    fun sendRedactModeFragment(redactModeFragment: IRedactModeFragment, dialogRedactMessage: IDialogRedactMessage<IMessage>)

    fun isRedactOff(): Boolean
}

interface IRedactModeForSendRequest{
    fun sendRedactModeFragment(dialogRedactMessage: IDialogRedactMessage<Chat>)
}

interface IRedactModeFragment {

    fun editModeClose()
}