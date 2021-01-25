package com.io.unknow.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.io.unknow.ui.fragment.messagefragment.TwoFieldFragment
import com.io.unknow.ui.fragment.ProfileFragment

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStatePagerAdapter(activity.supportFragmentManager){
    private val profileFragment = ProfileFragment()
    private val twoFieldFragment = TwoFieldFragment()

    /*override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position){
            0 -> profileFragment
            else -> TwoFieldFragment()
        }*/

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment =
        when (position){
            0 -> profileFragment
            else -> twoFieldFragment
        }


}