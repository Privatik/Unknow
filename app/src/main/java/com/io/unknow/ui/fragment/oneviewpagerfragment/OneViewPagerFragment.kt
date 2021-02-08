package com.io.unknow.ui.fragment.oneviewpagerfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.livedata.OneFieldLiveData
import com.io.unknow.navigation.IExit
import com.io.unknow.navigation.IScrooll
import com.io.unknow.ui.fragment.twoviewpagerfragment.ChatListFragment
import com.io.unknow.ui.fragment.twoviewpagerfragment.EmptyFragment
import com.io.unknow.viewmodel.fragment.OneFieldViewModel
import com.io.unknow.viewmodel.fragment.TwoFieldViewModel


class OneViewPagerFragment(val y: Int) : Fragment() , IExit{
    private lateinit var viewModel: OneFieldViewModel
    private lateinit var scrooll: IScrooll

    override fun onAttach(context: Context) {
        super.onAttach(context)

        scrooll = context as IScrooll
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OneFieldViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.online(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)
        val layout = view.findViewById<FrameLayout>(R.id.field)

        viewModel.liveData.observeForever {
            if (it != null) {
                if (layout.visibility == View.GONE) {
                    layout.visibility = View.VISIBLE
                    loadingProgressBar.visibility = View.GONE
                }

                if (it.isBan) {
                    childFragmentManager.beginTransaction()
                        .add(R.id.field, BanFragment(this)).commit()
                    scrooll.isScrollPager(false)
                } else {
                    childFragmentManager.beginTransaction()
                        .add(R.id.field, createProfileFragment()).commit()
                }
            }
        }
    }

    private fun createProfileFragment(): ProfileFragment {
        val args = Bundle()
        args.putInt("scroll", y)


        val profileFragment = ProfileFragment(this)
        profileFragment.arguments = args

        return profileFragment
    }

    override fun onDestroy() {
        viewModel.online(false)
        super.onDestroy()
    }

    override fun exit() {
        viewModel.exit()
    }
}