package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.io.unknow.R
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IDialogRedactMessage
import com.io.unknow.parse.MessageParse

class EditMessageDialogFragment: DialogFragment() {

    private lateinit var dialogRedactMessage: IDialogRedactMessage

    companion object{
        private const val TAG_MY_MESSAGE = "myMessage"
        private const val TAG_MESSAGE = "Message"

        fun newInstance(isNotMyMessage: Boolean, message: IMessage):EditMessageDialogFragment{
            val args = Bundle()
            args.putBoolean(TAG_MY_MESSAGE,isNotMyMessage)
            args.putSerializable(TAG_MESSAGE,message)

            val fragment = EditMessageDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogRedactMessage = context as IDialogRedactMessage
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.fragmnet_edit_message,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = MessageParse.getMessageFromInterface(arguments?.getSerializable(TAG_MESSAGE) as IMessage)!!


        val edit = view.findViewById<Button>(R.id.edit)
        val delete = view.findViewById<Button>(R.id.delete)

        if (arguments?.getBoolean(TAG_MY_MESSAGE)!!){
            edit.visibility = View.GONE
        }
        else{
            edit.setOnClickListener {
                dialogRedactMessage.edit(message = message)
            }
        }

        delete.setOnClickListener {
            if (CurrentUser.user?.id == message.userId) {
                dialog?.cancel()
                DeleteConfirmationDialogFragment.newInstance(message = message).show(requireParentFragment().childFragmentManager.beginTransaction(),"Confirmation")
            } else {
                dialogRedactMessage.delete(message = message, isDeleteForAll = false)
            }
        }
    }
}