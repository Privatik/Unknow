package com.io.unknow.ui.dialogfragment

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.databinding.FragmentSearchUserBinding
import com.io.unknow.viewmodel.dialogfragment.DialogWithUserViewModel
import com.io.unknow.viewmodel.dialogfragment.SearchUserViewModel

class SearchUserDialogFragment: DialogFragment(){
    private lateinit var binding: FragmentSearchUserBinding
    private val TAG = "dialogWithUser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding = FragmentSearchUserBinding.inflate(inflater,container,false)
        binding.viewmodel = ViewModelProviders.of(this).get(SearchUserViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnClickListener {
            binding.viewmodel?.find()
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.field.visibility = View.GONE
        }

        binding.search.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.search.setBackgroundResource(R.drawable.background_button_selected)
                binding.search.setTextColor(resources.getColor(R.color.ebony_clay))
            }
            else if (event.action == MotionEvent.ACTION_UP) {
                binding.search.setBackgroundResource(R.drawable.background_button_normal)
                binding.search.setTextColor(resources.getColor(R.color.dark_pink))
            }
            return@setOnTouchListener false
        }

        binding.viewmodel!!.liveData.observeForever {
            dialog?.cancel()
            val key = it.keys.first()
            DialogWithUserFragment.newInstance(it.getValue(key),key).show(childFragmentManager,TAG)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        binding.viewmodel?.close()
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null)
        {
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}