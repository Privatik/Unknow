package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.R
import com.io.unknow.adapter.DialogAdapter
import com.io.unknow.databinding.ChatLayoutBinding
import com.io.unknow.model.Message
import com.io.unknow.viewmodel.dialogfragment.DialogWithUserViewModel

class DialogWithUserFragment: DialogFragment() {

    private lateinit var binding: ChatLayoutBinding
    private var mContext: Context? = null

    companion object{
        private const val ID_MESSAGE = "messages"
        private const val ID_USER = "user"
        private const val TAG = "Dialog"

        fun newInstance(messageId: String, userId: String):DialogWithUserFragment {
            val args = Bundle()
            args.putString(ID_MESSAGE,messageId)
            args.putString(ID_USER,userId)

            Log.i(TAG,"newInst")
            val fragment = DialogWithUserFragment()
            fragment.arguments = args
            return fragment
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

        val userId = arguments?.getString(ID_USER)!!
        arguments?.getString(ID_MESSAGE,"")?.let { binding.viewModel!!.loadMessages(it, userId) }
        binding.name.text = userId

        val recyclerView = view.findViewById<RecyclerView>(R.id.messages_field)
        val send = view.findViewById<ImageButton>(R.id.send)
        val editText =  view.findViewById<EditText>(R.id.edit)

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

       binding.viewModel!!.liveData.observeForever {list ->
               val adapter = DialogAdapter(mContext!!, list, userId)
               binding.viewModel!!.initAdapter(adapter)

               recyclerView.adapter = adapter

               binding.viewModel!!.loadCallback()
               Log.i("LoadDialog","init adapter")
                   // recyclerView.smoothScrollToPosition(adapter!!.itemCount - 1)
       }

        binding.back.setOnClickListener { dialog?.cancel() }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.i(TAG,"Cancel Dialog")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"Stop Dialog")
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}