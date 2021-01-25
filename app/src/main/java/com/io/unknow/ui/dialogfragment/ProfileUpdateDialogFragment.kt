package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.databinding.FragmentProfileUpdateBinding
import com.io.unknow.navigation.IUpdateUser
import com.io.unknow.util.ToastMessage
import com.io.unknow.viewmodel.dialogfragment.ProfileUpdateViewModel

class ProfileUpdateDialogFragment: DialogFragment() {

    private lateinit var binding: FragmentProfileUpdateBinding
    private var mContext: Context? = null

    companion object{
        private const val HEIGHT = "height"
        private const val WEIGHT = "weight"
        private const val UPDATE = "UPDATE"

        fun newInstance(updateUser: IUpdateUser, height: String, weight: String):ProfileUpdateDialogFragment {
            val args = Bundle()
            args.putString(HEIGHT, height)
            args.putString(WEIGHT, weight)
            args.putSerializable(UPDATE,updateUser)

            val fragment = ProfileUpdateDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding = FragmentProfileUpdateBinding.inflate(inflater,container,false)
        binding.viewmodel = ViewModelProviders.of(this).get(ProfileUpdateViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel?.height = arguments?.getString(HEIGHT) ?: ""
        binding.viewmodel?.weight = arguments?.getString(WEIGHT) ?: ""
        val iUpateUser = arguments?.getSerializable(UPDATE) as IUpdateUser

        binding.save.setOnClickListener {
            Log.i("Update","click")
            if (editCorrect()) {
                iUpateUser.update(binding.viewmodel?.height!!, binding.viewmodel?.weight!!)
                dialog?.cancel()
            }
        }

        binding.editHeight.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.editHeight.setSelection(binding.viewmodel?.height!!.length)
            }
        }

        binding.editWeight.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.editWeight.setSelection(binding.viewmodel?.weight!!.length)
            }
        }

    }

    fun editCorrect():Boolean{
        if (binding.editHeight.text.toString() != "") {
            if (binding.editHeight.text.toString().toInt() <= 100) {
                ToastMessage.message(
                    mContext!!,
                    "Начальный рост слишком маленький(Минимальный рост 100 см)"
                )
                return false
            }
            if (binding.editHeight.text.toString().toInt() >= 220) {
                ToastMessage.message(
                    mContext!!,
                    "Конечный рост слишком слишком большой(Максимальный рост 220 см)"
                )
                return false
            }
        }
        if (binding.editWeight.text.toString() != "") {
            if (binding.editWeight.text.toString().toInt() <= 20) {
                ToastMessage.message(
                    mContext!!,
                    "Начальный вес слишком маленький(Минимальный вес 20 кг)"
                )
                return false
            }
            if (binding.editWeight.text.toString().toInt() >= 200) {
                ToastMessage.message(
                    mContext!!,
                    "Конечный вес слишком большой(Максимальный вес 200 кг)"
                )
                return false
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null)
        {
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}