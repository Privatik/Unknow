package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.io.unknow.R
import com.io.unknow.navigation.IDialogRedactMessage

open class EditDialogFragment<T>: DialogFragment() {
    protected lateinit var dialogRedactMessage: IDialogRedactMessage<T>

    protected lateinit var edit:Button
    protected lateinit var delete:Button

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogRedactMessage = context as IDialogRedactMessage<T>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.fragmnet_edit_message,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        edit = view.findViewById(R.id.edit)
        delete = view.findViewById(R.id.delete)

    }
}