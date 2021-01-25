package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.R
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.databinding.ChatLayoutBinding
import com.io.unknow.model.Chat
import com.io.unknow.model.Message
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.parse.CalendarParse
import com.io.unknow.util.ToastMessage
import com.io.unknow.util.UpdateDateToolbar
import com.io.unknow.viewmodel.dialogfragment.DialogWithUserViewModel

class DialogWithUserFragment: DialogFragment() , IUpdateDialog{

    private lateinit var binding: ChatLayoutBinding
    private var mContext: Context? = null

    companion object{
        private const val ID_CHAT = "chat"
        private const val ID_USER = "user"
        private const val TAG = "Dialog"

        fun newInstance(chat: Chat, userId: String):DialogWithUserFragment {
            val args = Bundle()
            args.putSerializable(ID_CHAT, chat)
            args.putString(ID_USER,userId)

            Log.i(TAG,"newInst")
            val fragment = DialogWithUserFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(context: Context, userId: String): Intent{
            val intent = Intent(context, DialogWithUserFragment::class.java)
                intent.putExtra(ID_USER, userId)

            return intent
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.FullScreenDialogStyle)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding = ChatLayoutBinding.inflate(inflater,container,false)
        binding.viewModel = ViewModelProviders.of(this).get(DialogWithUserViewModel::class.java)

        Log.i(TAG,"onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userId: String
        var chat: Chat?
        try {
            userId = arguments?.getString(ID_USER)!!
            chat = arguments?.getSerializable(ID_CHAT) as Chat
        }catch (e: Exception){
            val intent = activity?.intent
            userId = intent?.getStringExtra(ID_USER)!!
            chat = null
        }

        binding.viewModel!!.loadMessages(chat, userId, this)

        binding.name.text = userId

        val recyclerView = binding.messagesField
        val send = binding.send
        val editText =  binding.edit

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.hasFixedSize()

        send.setOnClickListener {
            if (editText.text.trim().isNotEmpty()){
                binding.viewModel?.sendMessage(editText.text.toString())
                editText.setText("")
            }
        }

        Log.i(TAG,"onCreateView")

        var adapter: DialogAdapter? = null
        binding.viewModel!!.liveData.observeForever {list ->
               adapter = DialogAdapter(mContext!!, list, userId)
               binding.viewModel!!.initAdapter(adapter!!)

               recyclerView.adapter = adapter

               binding.viewModel!!.loadCallback()
               Log.i("LoadDialog","init adapter")
                   // recyclerView.smoothScrollToPosition(adapter!!.itemCount - 1)
       }

        val updateDateToolbar = UpdateDateToolbar(mContext!!,adapter, layoutManager)
        binding.messagesField.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            private var date: String = ""

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                date = updateDateToolbar.onScroll(date,dy)
            }
        })

        binding.back.setOnClickListener { dialog?.cancel() }
    }

    override fun onStart() {
        super.onStart()
        binding.viewModel?.changeDialog(true)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.i(TAG,"Cancel Dialog")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"Stop Dialog")
        binding.viewModel?.changeDialog(false)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun update(size: Int) {
        binding.messagesField.smoothScrollToPosition(size)
    }
}