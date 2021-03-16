package com.io.unknow.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.io.unknow.R

class EditMessageDialogFragment: DialogFragment() {

    companion object{
        private const val TAG_MY_MESSAGE = "myMessage"

        fun newInstance(isNotMyMessage: Boolean):EditMessageDialogFragment{
            val args = Bundle()
            args.putBoolean(TAG_MY_MESSAGE,isNotMyMessage)

            val fragment = EditMessageDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragmnet_edit_message,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val edit = view.findViewById<Button>(R.id.edit)

        if (arguments?.getBoolean(TAG_MY_MESSAGE)!!){
            edit.visibility = View.GONE
        }
        else{
            edit.setOnClickListener {

            }
        }

        view.findViewById<Button>(R.id.delete)
    }
}