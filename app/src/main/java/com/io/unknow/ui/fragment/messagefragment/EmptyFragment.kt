package com.io.unknow.ui.fragment.messagefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.io.unknow.R
import com.io.unknow.navigation.ICreateDialog
import com.io.unknow.ui.dialogfragment.DialogWithUserFragment
import com.io.unknow.ui.dialogfragment.SearchUserDialogFragment

class EmptyFragment: Fragment(), ICreateDialog {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<ImageButton>(R.id.fab)

        fab.setOnClickListener {
            SearchUserDialogFragment.newInstance(this).show(childFragmentManager,"search")
        }
    }

    override fun open(messageId: String, userId: String) {
        DialogWithUserFragment.newInstance(messageId, userId).show(childFragmentManager,"dialogWithUser")
    }
}