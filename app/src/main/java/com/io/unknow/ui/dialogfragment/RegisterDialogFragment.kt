package com.io.unknow.ui.dialogfragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.io.unknow.R
import com.io.unknow.databinding.FragmentRegisterBinding
import com.io.unknow.model.User
import com.io.unknow.navigation.ICreateUser
import com.io.unknow.parse.DataParse
import com.io.unknow.util.ToastMessage.message
import com.io.unknow.viewmodel.dialogfragment.RegisterViewModel
import java.util.*

class RegisterDialogFragment: DialogFragment(){

    private lateinit var createUser: ICreateUser
    private lateinit var binding: FragmentRegisterBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createUser = context as ICreateUser
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
        dialog?.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        binding.viewmodel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RadioButton>(R.id.men)
        view.findViewById<RadioButton>(R.id.women)
        view.findViewById<RadioGroup>(R.id.radioGroup)

        initButton(view.findViewById(R.id.back))
        initButton(view.findViewById(R.id.register))

        initDate()

    }

    private fun initDate() {
        val calendarEnd = Calendar.getInstance()
        val calendarStart = Calendar.getInstance()

        calendarStart.set(Calendar.YEAR,calendarStart.get(Calendar.YEAR) - 100)

        binding.age.maxDate = calendarEnd.timeInMillis
        binding.age.minDate = calendarStart.timeInMillis
        binding.age.init(calendarEnd.get(Calendar.YEAR),calendarEnd.get(Calendar.MONTH),calendarEnd.get(Calendar.DAY_OF_MONTH),null)
    }

    private fun getCalendar():Date{
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, binding.age.year)
        calendar.set(Calendar.MONTH, binding.age.month)
        calendar.set(Calendar.DAY_OF_MONTH, binding.age.dayOfMonth)

        return Date(calendar.timeInMillis)
    }

    private fun validable(): Boolean {
        if (binding.loginEdit.text.isEmpty()){
            message(requireContext(),"Login is empty")
            return false
        }
        if (binding.passwordEdit.text.isEmpty()){
            message(requireContext(),"Passwoed is empty")
            return false
        }
        if (binding.passwordEditRepeat.text.isEmpty()){
            message(requireContext(),"PasswoedRepeat is empty")
            return false
        }
        if (binding.passwordEditRepeat.text.toString() != binding.passwordEdit.text.toString()){
            message(requireContext(),"Passwords do not match")
            return false
        }
        return true
    }


    @SuppressLint("ClickableViewAccessibility")
    fun initButton(button: Button){
        button.setOnClickListener {
            if (button.id == R.id.back) {
                dialog?.cancel()
            }
            else if (button.id == R.id.register){
                if (validable()) {
                    createUser.createAccount(
                        binding.loginEdit.text.toString(),
                        binding.passwordEdit.text.toString(),
                        User("", binding.viewmodel!!.isMenSex,DataParse.getString(getCalendar()))
                    )
                    dialog?.cancel()
                }
            }
        }

        button.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                button.isSelected = true
            }
            else if (event.action == MotionEvent.ACTION_UP) {
                button.isSelected = false
            }
            return@setOnTouchListener false
        }
    }
}