package com.io.unknow.ui.fragment.messagefragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.unknow.R
import com.io.unknow.adapter.AdapterChats
import com.io.unknow.model.Chat
import com.io.unknow.ui.dialogfragment.SearchUserDialogFragment
import com.io.unknow.viewmodel.fragment.ChatListViewModel
import com.io.unknow.viewmodel.fragment.ProfileViewModel
import java.io.Serializable

class ChatListFragment: Fragment() {
    companion object{
        private val ID_CHATS = "chats"

        fun newInstance(map: Map<String,Chat>): ChatListFragment{
            val args = Bundle()
            args.putSerializable(ID_CHATS,map as Serializable)

            val fragment = ChatListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list_chat,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listChats = arguments?.getSerializable(ID_CHATS) as Map<String, Chat>

        val chats = view.findViewById<RecyclerView>(R.id.chats)
        chats.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        chats.adapter = AdapterChats(listChats,this)
        chats.hasFixedSize()

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            SearchUserDialogFragment().show(childFragmentManager,"search")
        }
    }
}