package com.io.unknow.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.model.User
import com.io.unknow.navigation.ICreateUser
import com.io.unknow.util.ToastMessage.message
import com.io.unknow.viewmodel.dialogfragment.RegisterViewModel
import kotlin.random.Random

class RegisterDialogFragment: DialogFragment(){

    private lateinit var createUser: ICreateUser
    private lateinit var viewModel: RegisterViewModel

    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var passwordRepeat: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createUser = context as ICreateUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        setStyle(STYLE_NORMAL,R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return  inflater.inflate(R.layout.fragment_register,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login = view.findViewById<EditText>(R.id.loginEdit)
        password = view.findViewById<EditText>(R.id.passwordEdit)
        passwordRepeat = view.findViewById<EditText>(R.id.passwordEditRepeat)

        val radioButtonMen = view.findViewById<RadioButton>(R.id.men)
        val radioButtonWomen = view.findViewById<RadioButton>(R.id.women)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        initButton(view.findViewById(R.id.back))
        initButton(view.findViewById(R.id.register))

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                -1 -> { if (viewModel.isMenSex){initRadioButton(radioButtonMen, radioButtonWomen)}}
                R.id.men -> {viewModel.isMenSex = true}
                R.id.women -> {viewModel.isMenSex = false}
                else ->{}
            }
        }

        initRadioButton(radioButtonMen,radioButtonWomen)
        initEditText()
    }

    private fun initRadioButton(radioButtonMen: RadioButton, radioButtonWomen: RadioButton) {
        if (viewModel.isMenSex) radioButtonMen.isChecked = true
        else radioButtonWomen.isChecked = true
    }

    private fun initEditText() {
        login.setText(viewModel.login)
        password.setText(viewModel.password)
        passwordRepeat.setText(viewModel.passwordRepea)
    }

    private fun validable(): Boolean {
        if (login.text.isEmpty()){
            message(requireContext(),"Login is empty")
            return false
        }
        if (password.text.isEmpty()){
            message(requireContext(),"Passwoed is empty")
            return false
        }
        if (passwordRepeat.text.isEmpty()){
            message(requireContext(),"PasswoedRepeat is empty")
            return false
        }
        if (passwordRepeat.text.toString() != password.text.toString()){
            message(requireContext(),"Passwords do not match")
            return false
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.login = login.text.toString()
        viewModel.password = password.text.toString()
        viewModel.passwordRepea = passwordRepeat.text.toString()

    }


    fun initButton(button: Button){
        button.setOnClickListener {
            if (button.id == R.id.back) {
                dialog?.cancel()
            }
            else if (button.id == R.id.register){
                if (validable()) {
                    createUser.createAccount(
                        login.text.toString(),
                        password.text.toString(),
                        User("", viewModel.sex())
                    )
                    dialog?.cancel()
                }
            }
        }

        button.setOnTouchListener { v, event ->
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