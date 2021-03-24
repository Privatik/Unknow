package com.io.unknow.navigation

interface IRedactModeActivity {

    fun redactModeOn()
    fun redactModeOff()

    fun sendRedactModeFragment(redactModeFragment: IRedactModeFragment, dialogRedactMessage: IDialogRedactMessage)

    fun isRedactOff(): Boolean
}

interface IRedactModeFragment {

    fun editModeClose()
}