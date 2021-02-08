package com.io.unknow.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.analytics.FirebaseAnalytics
import com.io.unknow.R
import com.io.unknow.adapter.ViewPagerAdapter
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.navigation.*
import com.io.unknow.push.PushNotification
import com.io.unknow.util.Setting
import java.lang.NullPointerException

private val TAG = MainActivity::class.simpleName
class MainActivity : AppCompatActivity() , IMainExit , IScrooll, ISetting, IUpdateSwipe, IUpdateActivity {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var viewPager: ViewPager? = null
   // private lateinit var pushNotification: PushNotification
    private var pushTwoFragment: IPushTwoFragment? = null
    private var pushProfileFragment: IPushProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        //pushNotification = PushNotification(this)

        viewPager = findViewById(R.id.pager)
        viewPager?.adapter = ViewPagerAdapter(this, intent.getIntExtra("scroll", 0))

        viewPager?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 1) {
                    pushTwoFragment?.initViewTwoFragment()
                }
            }

            override fun onPageSelected(position: Int) {}

        })

        //startActivity(Intent(this, LoginActivty))
    }


    override fun exit() {
        CurrentUser.user = null
        startActivity(Intent(this, LoginActivty::class.java))
        finish()
    }

    override fun isScrollPager(isPager: Boolean) {
        if (isPager) {
            viewPager?.setOnTouchListener(null)
        } else {
            viewPager?.setOnTouchListener { v, event ->
                return@setOnTouchListener true
            }
        }
    }

    override fun onResume() {
        super.onResume()
      /*  try {
            pushNotification.cancelAlarm()
        } catch (e: Exception) {
        }*/
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "OnStop")
       // pushNotification.createAlarm()
    }

    override fun update(pushTwoFragment: IPushTwoFragment) {
        this.pushTwoFragment = pushTwoFragment
    }

    override fun update(pushProfileFragment: IPushProfileFragment) {
        this.pushProfileFragment = pushProfileFragment
    }

    override fun updateSetting() {
        Log.i("ProfileFragment","updateSetting ${pushProfileFragment?.scrollViewGetY()}")
        startActivity(Intent(this, MainActivity::class.java).putExtra("scroll", pushProfileFragment?.scrollViewGetY()))
        viewPager = null
        finish()
    }
}
