package com.io.unknow.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.io.unknow.R
import com.io.unknow.adapter.ViewPagerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = ViewPagerAdapter(this)

        //startActivity(Intent(this, LoginActivty))
    }
}