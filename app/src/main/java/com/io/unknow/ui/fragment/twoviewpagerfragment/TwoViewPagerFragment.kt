package com.io.unknow.ui.fragment.twoviewpagerfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.io.unknow.R
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IPushTwoFragment
import com.io.unknow.navigation.IUpdateChatList
import com.io.unknow.navigation.IUpdateSwipe
import com.io.unknow.viewmodel.fragment.TwoFieldViewModel


class TwoViewPagerFragment : Fragment(), IPushTwoFragment, IUpdateChatList{

    private lateinit var viewModel: TwoFieldViewModel
    private var onSwipe = false
    private lateinit var layout: FrameLayout
    private lateinit var loadingProgressBar : ProgressBar
    private lateinit var updateSwipe: IUpdateSwipe
    private var chatMap : MutableMap<String,Chat>? = null

    init {
        Log.i("TwoField","new")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        updateSwipe = context as IUpdateSwipe
        Log.i("TwoField","onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TwoFieldViewModel::class.java)
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TwoField","onViewCreated")

        loadingProgressBar = view.findViewById(R.id.loading_progress_bar)
        layout = view.findViewById(R.id.messages_field)

        updateSwipe.update(this)

        viewModel.liveData.observeForever {
            Log.i("TwoFieldFragment","${childFragmentManager.fragments.size}")
            chatMap = it
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("TwoField","onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("TwoField","onStop")
    }

    override fun initViewTwoFragment() {
        if (chatMap != null && !onSwipe) {

            if (layout.visibility == View.GONE) {
                layout.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.GONE
            }

            onSwipe = true

            if (chatMap!!.isEmpty()) {
                childFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.messages_field, EmptyFragment()).commit()
            } else {
                childFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.messages_field, ChatListFragment.newInstance(chatMap!!)).commit()
            }

        }
    }

    override fun update() {
        childFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.messages_field, EmptyFragment()).commit()
    }


}