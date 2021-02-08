package com.io.unknow.ui.fragment.oneviewpagerfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.io.unknow.R
import com.io.unknow.navigation.IExit
import com.io.unknow.navigation.IMainExit


class BanFragment(val exit: IExit) : Fragment() {
    private lateinit var mainExit: IMainExit

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainExit = context as IMainExit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_ban, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitBan = view.findViewById<Button>(R.id.exitBan)

        exitBan.setOnClickListener {
            exit.exit()
            mainExit.exit()
        }

    }


}