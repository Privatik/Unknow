package com.io.unknow.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.databinding.ActivityLoginBinding
import com.io.unknow.model.User
import com.io.unknow.navigation.ICreateUser
import com.io.unknow.navigation.IScrooll
import com.io.unknow.ui.dialogfragment.RegisterDialogFragment
import com.io.unknow.util.Setting
import com.io.unknow.viewmodel.activity.LoginViewModel


class LoginActivty: AppCompatActivity(), ICreateUser{

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        setContentView(binding.root)

        binding.viewmodel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initButton(findViewById(R.id.login_in))
        initButton(findViewById(R.id.register))

        binding.viewmodel?.initBaseAuth(this)
    }

    override fun onStart() {
        super.onStart()
        binding.viewmodel?.addListener()
    }

    override fun onStop() {
        super.onStop()
        binding.viewmodel?.removeListener()
    }

    override fun createAccount(email: String, password: String, user: User) {
        binding.viewmodel?.createUser(email, password, user)
        Log.i("Login","${email} ${password} ${user}")
    }

    fun initButton(button: Button){
        button.setOnClickListener {
            if (button.id == R.id.login_in) {
                binding.viewmodel?.singIn(this)
            }
            else if (button.id == R.id.register){
                RegisterDialogFragment().show(supportFragmentManager,"register")
            }
        }
    }
}