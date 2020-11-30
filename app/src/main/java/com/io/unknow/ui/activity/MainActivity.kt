package com.io.unknow.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.analytics.FirebaseAnalytics
import com.io.unknow.R
import com.io.unknow.adapter.ViewPagerAdapter
import com.io.unknow.navigation.IMainExit
import com.io.unknow.navigation.IScrooll


class MainActivity : AppCompatActivity() , IMainExit , IScrooll{

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = ViewPagerAdapter(this)

        //startActivity(Intent(this, LoginActivty))
    }

    override fun exit() {
        startActivity(Intent(this, LoginActivty::class.java))
        finish()
    }

    override fun isScrollPager(isPager: Boolean) {
        if (isPager){
            viewPager.setOnTouchListener(null)
        }
        else{
            viewPager.setOnTouchListener { v, event ->
                return@setOnTouchListener true
            }
        }
    }
}