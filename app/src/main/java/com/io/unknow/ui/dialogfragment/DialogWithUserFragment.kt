package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.databinding.ChatLayoutBinding
import com.io.unknow.databinding.IncludeBottomsheetbehaviorChatLayputBinding
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IBottomSheet
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.util.UpdateDateToolbar
import com.io.unknow.viewmodel.dialogfragment.DialogWithUserViewModel

private const val GALLERY = "Gallery"
class DialogWithUserFragment: Fragment() , IUpdateDialog {

    private lateinit var binding: ChatLayoutBinding
    private var mContext: Context? = null
    private lateinit var buttomSheet: IBottomSheet

    companion object {
        private const val ID_CHAT = "chat"
        private const val ID_USER = "user"
        private const val TAG = "Dialog"

        fun newInstance(chat: Chat, userId: String): DialogWithUserFragment {
            val args = Bundle()
            args.putSerializable(ID_CHAT, chat)
            args.putString(ID_USER, userId)

            Log.i(TAG, "newInst")
            val fragment = DialogWithUserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        buttomSheet = context as IBottomSheet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding = ChatLayoutBinding.inflate(inflater, container, false)
        binding.viewModel = ViewModelProvider(this).get(DialogWithUserViewModel::class.java)

        Log.i(TAG, "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chat: Chat = arguments?.getSerializable(ID_CHAT) as Chat
        val userId: String = arguments?.getString(ID_USER)!!


        binding.viewModel!!.loadMessages(chat, userId, this)
        binding.name.text = userId


        binding.field.send.setOnClickListener {
            if (binding.field.edit.text.trim().isNotEmpty()) {
                binding.viewModel?.sendMessageText(binding.field.edit.text.toString())
                binding.field.edit.setText("")
            }
        }


        binding.field.add.setOnClickListener {
              buttomSheet.loadBottomSheet()
        }


        binding.back.setOnClickListener { activity?.onBackPressed() }

        Log.i(TAG, "onCreateView")


        initLifeData(userId)
    }


    private fun initLifeData(userId: String){
        var adapter: DialogAdapter?
        binding.viewModel!!.liveData.observeForever { list ->
            adapter = DialogAdapter(mContext!!, list, userId, childFragmentManager)
            binding.viewModel!!.initAdapter(adapter!!)
            initRecycleView(adapter!!)

            binding.field.recyclerviewMessages.adapter = adapter

            binding.viewModel!!.loadCallback()
            Log.i("LoadDialog", "init adapter")
            // recyclerView.smoothScrollToPosition(adapter!!.itemCount - 1)
        }

        binding.viewModel!!.liveDataOnline.observeForever {
            binding.status.text = if (it) "online" else "offline"
        }

    }

    private fun initRecycleView(adapter: DialogAdapter){
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.field.recyclerviewMessages.layoutManager = layoutManager
        binding.field.recyclerviewMessages.hasFixedSize()

        val updateDateToolbar = UpdateDateToolbar(mContext!!, adapter, layoutManager)
        binding.field.recyclerviewMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var date: String = ""

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                date = updateDateToolbar.onScroll(date)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        binding.viewModel?.changeDialog(true)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "Stop Dialog")
        binding.viewModel?.changeDialog(false)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun update(size: Int) {
        binding.field.recyclerviewMessages.smoothScrollToPosition(size)
    }

}