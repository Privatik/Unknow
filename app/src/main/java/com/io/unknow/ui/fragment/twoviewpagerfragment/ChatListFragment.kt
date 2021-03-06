package com.io.unknow.ui.fragment.twoviewpagerfragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.unknow.R
import com.io.unknow.adapter.AdapterChats
import com.io.unknow.model.Chat
import com.io.unknow.navigation.ICreateDialog
import com.io.unknow.navigation.IDialogRedactMessage
import com.io.unknow.navigation.IRedactModeForSendRequest
import com.io.unknow.navigation.IUpdateChatList
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.ui.dialogfragment.SearchUserDialogFragment
import com.io.unknow.viewmodel.fragment.ChatListViewModel
import java.io.Serializable

class ChatListFragment: Fragment(), ICreateDialog, IDialogRedactMessage<Chat>{
    private var adapter: AdapterChats? = null
    private lateinit var motionLayout: MotionLayout
    private lateinit var redactForSendRequest: IRedactModeForSendRequest
    private lateinit var updateChatList: IUpdateChatList

    private lateinit var viewModel: ChatListViewModel
    private lateinit var map: MutableMap<String, Chat>

    companion object{
        private const val ID_CHATS = "chats"

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

        redactForSendRequest = context as IRedactModeForSendRequest
        updateChatList = context as IUpdateChatList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_list_chat,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        redactForSendRequest.sendRedactModeFragment(this)

        val listChats = arguments?.getSerializable(ID_CHATS) as MutableMap<String, Chat>

        val chats = view.findViewById<RecyclerView>(R.id.chats)
        chats.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        map = mutableMapOf()
        map.putAll(listChats)
        adapter = AdapterChats(map,this)
        chats.adapter = adapter
        chats.hasFixedSize()

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val searchChat = view.findViewById<EditText>(R.id.search_chat)

        motionLayout = view.findViewById(R.id.motion_base)

        motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId){
                    R.id.expanded -> {
                        Log.i("Motion","open")
                        searchChat.isEnabled = true
                    }
                    R.id.collapsed -> {
                        Log.i("Motion","close")
                        searchChat.isEnabled = false
                    }
                }
            }
        })

        fab.setOnClickListener {
            SearchUserDialogFragment.newInstance(this).show(childFragmentManager,"search")
        }

        filterEdit(searchChat, listChats)
    }

    private fun filterEdit(editText: EditText, list: MutableMap<String, Chat>){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val newList = list.filter{ it.key.contains(s,true) } as MutableMap
                adapter?.updateList(newList)
                Log.i("Edit","$newList $list ")
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onStart() {
        super.onStart()
        adapter?.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        if (motionLayout.progress > 0.5f){
            motionLayout.progress = 1f
        }else{
            motionLayout.progress = 0f
        }
    }

    override fun open(chat: Chat, userId: String) {
        DialogActivity.newInstance(context = requireContext(), chat = chat, userId = userId)
    }

    override fun delete(item: Chat) {
        if (map.size == 1){
            updateChatList.update()
        }

        val userId = map.filter { item == it.value }.map { it.key }.first()
        map.remove(userId)

        viewModel.deleteChat(userId = userId)
    }

    override fun delete(item: Chat, isDeleteForAll: Boolean) { }
    override fun edit(item: Chat) { }
}