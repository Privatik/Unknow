package com.io.unknow.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.io.unknow.R
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.Chat
import com.io.unknow.parse.MessageParse

class EditChatDialogFragment: EditDialogFragment<Chat>() {
    companion object{
        private const val TAG_CHAT = "CHAT"

        fun newInstance(chat: Chat):EditChatDialogFragment{
            val args = Bundle()
            args.putParcelable(TAG_CHAT,chat)

            val fragment = EditChatDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.fragmnet_edit_message,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chat = arguments?.getParcelable<Chat>(TAG_CHAT)!!

        edit.visibility = View.GONE

        delete.setOnClickListener {
            dialogRedactMessage.delete(item = chat, isDeleteForAll = false)
            dialog!!.cancel()
        }
    }
}