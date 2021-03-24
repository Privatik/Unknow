package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.io.unknow.R
import com.io.unknow.model.IMessage
import com.io.unknow.navigation.IDialogRedactMessage
import com.io.unknow.parse.MessageParse

class DeleteConfirmationDialogFragment : DialogFragment(){

    private lateinit var dialogRedactMessage: IDialogRedactMessage

    companion object{
        private const val TAG_MESSAGE = "Message"

        fun newInstance(message: IMessage):DeleteConfirmationDialogFragment{
            val args = Bundle()
            args.putSerializable(TAG_MESSAGE,message)

            val fragment = DeleteConfirmationDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogRedactMessage = context as IDialogRedactMessage
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dailog_delete_confirmation,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = MessageParse.getMessageFromInterface(arguments?.getSerializable(TAG_MESSAGE) as IMessage)!!

        val isDeleteCheckBox = view.findViewById<CheckBox>(R.id.is_delete)
        val deleteButton = view.findViewById<Button>(R.id.delete)

        deleteButton.setOnClickListener {
            dialogRedactMessage.delete(message = message, isDeleteForAll = isDeleteCheckBox.isChecked)
        }
    }
}