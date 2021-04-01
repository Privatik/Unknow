package com.io.unknow.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.databinding.ChatLayoutBinding
import com.io.unknow.decoration.TwoDateItemDecoration
import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.*
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.viewmodel.fragment.DialogWithUserViewModel

class DialogWithUserFragment: Fragment() , IUpdateDialog, ILoadImageFromGallery, IDialogRedactMessage<IMessage>, IRedactModeFragment {

    private lateinit var binding: ChatLayoutBinding
    private var mContext: Context? = null
    private lateinit var buttomSheet: IBottomSheet
    private var isTakeImages: Boolean = false
    private lateinit var redactMode: IRedactModeActivity


    private var messageRefactor: MessageText? = null

    companion object {
        private const val ID_CHAT = "chat"
        private const val ID_USER = "user"
        private const val TAG = "Dialog"

        fun newInstance(chat: Chat, userId: String): DialogWithUserFragment {
            val args = Bundle()
            args.putParcelable(ID_CHAT, chat)
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
        redactMode = context as IRedactModeActivity
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

        val chat: Chat = arguments?.getParcelable<Chat>(ID_CHAT)!!
        val userId: String = arguments?.getString(ID_USER)!!

        redactMode.sendRedactModeFragment(this,this)

        binding.viewModel!!.loadMessages(chat, userId, this)

        binding.send.setOnClickListener {
            Log.i("GALLERY","$isTakeImages")
            if (redactMode.isRedactOff()) {
                if (isTakeImages) {
                    val activity = activity as DialogActivity
                    if (activity.imagesSelected.isNotEmpty()) {
                        sendImages(activity.imagesSelected)
                        buttomSheet.closeBottomSheet()
                    }
                } else if (binding.edit.text.trim().isNotEmpty()) {
                    binding.viewModel?.sendMessageText(message = binding.edit.text.toString())
                    binding.edit.setText("")
                }
            } else if (binding.edit.text.trim().isNotEmpty()) {
                if (messageRefactor != null){
                    messageRefactor?.text = binding.edit.text.toString()
                    binding.viewModel?.updateMessage(messageText = messageRefactor!!)
                    messageRefactor = null
                    binding.edit.setText("")
                }
                redactMode.redactModeOff()
            }
        }


        binding.add.setOnClickListener {
            if (redactMode.isRedactOff()) {
                buttomSheet.loadBottomSheet()
            }
        }

        buttomSheet.loadInterfaceForLoadImages(loadImageFromGallery = this, sendButton =  binding.send)


        Log.i(TAG, "onCreateView")


        initLiveData(userId)
    }


    private fun initLiveData(userId: String){
        var adapter: DialogAdapter?
        binding.viewModel!!.liveData.observeForever { list ->
            adapter = DialogAdapter(mContext!!, list, userId, childFragmentManager, redactMode.isRedactOff())
            binding.viewModel!!.initAdapter(adapter!!)
            initRecycleView()

            binding.recyclerviewMessages.adapter = adapter


            binding.recyclerviewMessages.apply {addItemDecoration(
               // DateItemDecoration(this, adapter!!)
               TwoDateItemDecoration(binding.recyclerviewMessages, true, adapter!!.isHeader)
            )}

            binding.viewModel!!.loadCallback()
            Log.i("LoadDialog", "init adapter")
        }

    }

    private fun initRecycleView(){
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.recyclerviewMessages.layoutManager = layoutManager
        binding.recyclerviewMessages.hasFixedSize()

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
        binding.recyclerviewMessages.smoothScrollToPosition(size)
    }

    override fun sendImages(imagesList: List<String>) {
        imagesList.forEach {
            binding.viewModel?.sendMessagePhoto(it)
        }
    }

    override fun sendImage(imageUrl: String) {
        Log.i("CAMERA","sendImage")
        binding.viewModel?.sendMessagePhoto(imageUrl)
    }

    override fun isChangeBooleanSend(isChange: Boolean) {
        isTakeImages = isChange
    }

    override fun edit(item: IMessage) {
        if (item is MessageText) {
            binding.editMessage.visibility = View.VISIBLE
            binding.edit.setText(item.text)
        }
        if (buttomSheet.isBottomShow()){
            buttomSheet.closeBottomSheet()
        }
        messageRefactor = item as MessageText

        redactMode.redactModeOn()
    }

    override fun delete(item: IMessage, isDeleteForAll: Boolean) {
        binding.viewModel!!.deleteMessage(message = item, isDeleteForAll = isDeleteForAll)
    }

    override fun editModeClose() {
        binding.editMessage.visibility = View.GONE
        binding.edit.setText("")
        messageRefactor = null
    }

    override fun delete(item: IMessage) { }

}