package com.io.unknow.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.io.unknow.ui.fragment.oneviewpagerfragment.OneViewPagerFragment
import com.io.unknow.ui.fragment.twoviewpagerfragment.TwoViewPagerFragment
import com.io.unknow.ui.fragment.oneviewpagerfragment.ProfileFragment

class ViewPagerAdapter(activity: AppCompatActivity,val y: Int): FragmentPagerAdapter(activity.supportFragmentManager){


    fun isNoNull(): Boolean = y != 0
   /* override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position){
            0 -> ProfileFragment()
            else -> TwoFieldFragment()
        }*/

   override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment =
        when (position){
            0 -> if (isNoNull()) OneViewPagerFragment(y) else OneViewPagerFragment()
            else -> TwoViewPagerFragment()
        }


}