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
import com.io.unknow.decoration.DateItemDecoration
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IBottomSheet
import com.io.unknow.navigation.ILoadImageFromGallery
import com.io.unknow.navigation.IUpdateDialog
import com.io.unknow.ui.activity.DialogActivity
import com.io.unknow.viewmodel.dialogfragment.DialogWithUserViewModel

class DialogWithUserFragment: Fragment() , IUpdateDialog, ILoadImageFromGallery {

    private lateinit var binding: ChatLayoutBinding
    private var mContext: Context? = null
    private lateinit var buttomSheet: IBottomSheet
    private var isTakeImages: Boolean = false

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

        binding.send.setOnClickListener {
            Log.i("GALLERY","$isTakeImages")
            if (isTakeImages) {
                val activity = activity as DialogActivity
                if (activity.imagesSelected.isNotEmpty()) {
                    sendImages(activity.imagesSelected)
                    buttomSheet.closeBottomSheet()
                    activity.imagesSelected.clear()
                }
            }else if (binding.edit.text.trim().isNotEmpty()) {
                binding.viewModel?.sendMessageText(message = binding.edit.text.toString())
                binding.edit.setText("")
            }
        }


        binding.add.setOnClickListener {
              buttomSheet.loadBottomSheet()
        }

        buttomSheet.loadInterfaceForLoadImages(loadImageFromGallery = this, sendButton =  binding.send)


        Log.i(TAG, "onCreateView")


        initLiveData(userId)
    }


    private fun initLiveData(userId: String){
        var adapter: DialogAdapter?
        binding.viewModel!!.liveData.observeForever { list ->
            adapter = DialogAdapter(mContext!!, list, userId, childFragmentManager)
            binding.viewModel!!.initAdapter(adapter!!)
            initRecycleView()

            binding.recyclerviewMessages.adapter = adapter
            binding.recyclerviewMessages.apply {addItemDecoration(DateItemDecoration(this, adapter!!))}

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

}