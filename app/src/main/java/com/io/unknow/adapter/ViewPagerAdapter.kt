package com.io.unknow.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.io.unknow.ui.fragment.messagefragment.TwoFieldFragment
import com.io.unknow.ui.fragment.ProfileFragment

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity)  {
    private val profileFragment = ProfileFragment()
    private val messagesFragment = TwoFieldFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position){
            0 -> profileFragment
            else -> messagesFragment
        }

}