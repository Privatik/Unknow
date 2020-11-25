package com.io.unknow.ui.activity

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.model.User
import com.io.unknow.navigation.ICreateUser
import com.io.unknow.ui.dialogfragment.RegisterDialogFragment
import com.io.unknow.viewmodel.activity.LoginViewModel


class LoginActivty: AppCompatActivity(), ICreateUser{

    private lateinit var viewModel: LoginViewModel
    private lateinit var email: TextView
    private lateinit var password: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        email = findViewById(R.id.login)
        password = findViewById(R.id.password)

        initButton(findViewById(R.id.login_in))
        initButton(findViewById(R.id.register))

        viewModel.initBaseAuth(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.addListener()
    }

    override fun onStop() {
        super.onStop()
        viewModel.removeListener()
    }

    override fun createAccount(email: String, password: String, user: User) {
        viewModel.createUser(email, password, user)
    }

    fun initButton(button: Button){
        button.setOnClickListener {
            if (button.id == R.id.login_in) {
                viewModel.singIn(this, email.text.toString(), password.text.toString())
            }
            else if (button.id == R.id.register){
                RegisterDialogFragment().show(supportFragmentManager,"register")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.login = email.text.toString()
        viewModel.password = password.text.toString()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        email.text = viewModel.login
        password.text = viewModel.password
    }
}