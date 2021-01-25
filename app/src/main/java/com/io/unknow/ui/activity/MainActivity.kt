package com.io.unknow.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.analytics.FirebaseAnalytics
import com.io.unknow.R
import com.io.unknow.adapter.ViewPagerAdapter
import com.io.unknow.broadcast.NotificationListener
import com.io.unknow.navigation.IMainExit
import com.io.unknow.navigation.IScrooll
import com.io.unknow.navigation.ISetting
import com.io.unknow.util.Setting
import java.lang.NullPointerException

private val TAG = MainActivity::class.simpleName
class MainActivity : AppCompatActivity() , IMainExit , IScrooll, ISetting{

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var viewPager: ViewPager
    private val notificationBroadCast = NotificationListener()
    private lateinit var pendingIntent: PendingIntent
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        pendingIntent = PendingIntent.getBroadcast(this,0,Intent(this,NotificationListener::class.java),0)


//        scrollView = findViewById(R.id.scrollview)

  //      scrollView.scrollTo(0,intent.getIntExtra("update",0))

        viewPager = findViewById(R.id.pager)
        viewPager.adapter = ViewPagerAdapter(this)

        viewPager.offscreenPageLimit = 1
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

    override fun onResume() {
        super.onResume()
        try {
            cancelAlarm()
        }catch (e: Exception){}
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"OnStop")
        createAlarm()
    }

    override fun updateLanguage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun updateTheme(isBlack: Boolean) {
        Setting.updateTheme(isBlack)
    }

    override fun onSaveInstanceState(outState: Bundle) {}

    fun createAlarm(){
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent)

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent)
        } else {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent)
        }
    }

    fun cancelAlarm(){
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
    }
}