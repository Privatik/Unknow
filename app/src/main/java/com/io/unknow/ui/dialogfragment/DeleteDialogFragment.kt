package com.io.unknow.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.io.unknow.R

class DeleteDialogFragment: DialogFragment() {

    companion object{
        private const val TAG_MY_MESSAGE = "myMessage"

        fun newInstance(isNotMyMessage: Boolean):DeleteDialogFragment{
            val args = Bundle()
            args.putBoolean(TAG_MY_MESSAGE,isNotMyMessage)

            val fragment = DeleteDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_delete_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        val text = view.findViewById<TextView>(R.id.text)

        if (arguments?.getBoolean(TAG_MY_MESSAGE)!!){
            checkBox.visibility = View.GONE
            text.visibility = View.GONE
        }
    }
}