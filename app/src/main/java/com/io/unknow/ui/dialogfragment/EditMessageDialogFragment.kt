package com.io.unknow.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.io.unknow.R
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.IMessage
import com.io.unknow.parse.MessageParse

class EditMessageDialogFragment: EditDialogFragment<IMessage>() {

    companion object{
        private const val TAG_DO_NOT_EDIT = "don't need edit"
        private const val TAG_MESSAGE = "Message"

        fun newInstance(doNotNeedEdit: Boolean, message: IMessage):EditMessageDialogFragment{
            val args = Bundle()
            args.putBoolean(TAG_DO_NOT_EDIT,doNotNeedEdit)
            args.putParcelable(TAG_MESSAGE,message)

            val fragment = EditMessageDialogFragment()
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

        val message = MessageParse.getMessageFromInterface(arguments?.getParcelable(TAG_MESSAGE)!!)!!


        if (arguments?.getBoolean(TAG_DO_NOT_EDIT)!!){
            edit.visibility = View.GONE
        }
        else{
            edit.setOnClickListener {
                dialogRedactMessage.edit(item = message)
                dialog?.cancel()
            }
        }

        delete.setOnClickListener {
            if (CurrentUser.user?.id == message.userId) {
                DeleteConfirmationDialogFragment.newInstance(message = message).show(requireParentFragment().childFragmentManager.beginTransaction(),"Confirmation")
            } else {
                dialogRedactMessage.delete(item = message, isDeleteForAll = false)
            }
            dialog?.cancel()
        }
    }
}