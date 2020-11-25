package com.io.unknow.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.io.unknow.app.App
import javax.inject.Inject

class SplashScreen : AppCompatActivity() {

    @Inject lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)

        val user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{
            startActivity(Intent(this, LoginActivty::class.java))
        }
        finish()
    }
}