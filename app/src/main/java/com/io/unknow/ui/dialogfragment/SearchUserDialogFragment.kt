package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.io.unknow.databinding.FragmentSearchUserBinding
import com.io.unknow.model.Chat
import com.io.unknow.navigation.ICreateDialog
import com.io.unknow.util.ToastMessage
import com.io.unknow.viewmodel.dialogfragment.SearchUserViewModel

private const val TAG = "dialogWithUser"
class SearchUserDialogFragment: DialogFragment(){
    private lateinit var binding: FragmentSearchUserBinding
    private var mContext: Context? = null

    companion object{
        private const val CREATE_DIALOG = "createDialog"

        fun newInstance(createDialog: ICreateDialog):SearchUserDialogFragment {
            val args = Bundle()
            args.putSerializable(CREATE_DIALOG, createDialog)

            val fragment = SearchUserDialogFragment()
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

        binding = FragmentSearchUserBinding.inflate(inflater,container,false)
        binding.viewmodel = ViewModelProvider(this).get(SearchUserViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnClickListener {
            if (editCorrect()) {
                binding.viewmodel?.find()
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.field.visibility = View.GONE
            }
        }

        val createDialog = arguments?.getSerializable(CREATE_DIALOG) as ICreateDialog

        binding.viewmodel!!.liveData.observeForever {
            dialog?.cancel()
            val key = it.keys.first()
            Log.i("Dialog","createBase")
            createDialog.open(Chat(messages = it.getValue(key)),key)
        }

        //selectorEditText(binding.editAgeStart,binding.viewmodel?.searchString!!.ageStart)
        //selectorEditText(binding.editAgeEnd,binding.viewmodel?.searchString!!.ageEnd)
        //selectorEditText(binding.editHeightStart,binding.viewmodel?.searchString!!.heightStart)
        //selectorEditText(binding.editHeightEnd,binding.viewmodel?.searchString!!.heightEnd)
        //selectorEditText(binding.editWeightStart,binding.viewmodel?.searchString!!.weightStart)
        //selectorEditText(binding.editWeightEnd,binding.viewmodel?.searchString!!.weightEnd)


    }

    private fun editCorrect(): Boolean{
       if (binding.editAgeStart.text.toString() != "" && binding.editAgeStart.text.toString().toInt() <= 18) {
           ToastMessage.message(mContext!!, "Начальный возраст слишком маленький(Минимальное значение 18)")
           return false
       }
        if (binding.editAgeEnd.text.toString() != "" && binding.editAgeEnd.text.toString().toInt() >= 100) {
            ToastMessage.message(mContext!!, "Конечный возраст слишком большой(Максимальное значение 100)")
            return false
        }
        if (binding.editHeightStart.text.toString() != "" && binding.editHeightStart.text.toString().toInt() <= 100) {
            ToastMessage.message(mContext!!, "Начальный рост слишком маленький(Минимальный рост 100 см)")
            return false
        }
        if (binding.editHeightEnd.text.toString() != "" && binding.editHeightEnd.text.toString().toInt() >= 220) {
            ToastMessage.message(mContext!!, "Конечный рост слишком слишком большой(Максимальный рост 220 см)")
            return false
        }
        if (binding.editWeightStart.text.toString() != "" && binding.editWeightStart.text.toString().toInt() <= 20) {
            ToastMessage.message(mContext!!, "Начальный вес слишком маленький(Минимальный вес 20 кг)")
            return false
        }
        if (binding.editWeightEnd.text.toString() != "" && binding.editWeightEnd.text.toString().toInt() >= 200) {
            ToastMessage.message(mContext!!, "Конечный вес слишком большой(Максимальный вес 200 кг)")
            return false
        }
        return true
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.i("Dialog","Cancel Search")
    }

    override fun onStop() {
        super.onStop()
        binding.viewmodel?.close()
        Log.i("Dialog","Stop Search")
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