package com.io.unknow.ui.fragment.messagefragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.R
import com.io.unknow.viewmodel.fragment.TwoFieldViewModel
import kotlinx.android.synthetic.main.fragment_messages.*
import java.io.Serializable


class TwoFieldFragment : Fragment(){

    private lateinit var viewModel: TwoFieldViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("TwoField","onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(TwoFieldViewModel::class.java)
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)
        val layout = view.findViewById<FrameLayout>(R.id.messages_field)

        viewModel.liveData.observeForever {

            if (layout.visibility == View.GONE) {
                layout.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.GONE
            }
            Log.i("TwoFieldFragment","${childFragmentManager.fragments.size}")
            if (childFragmentManager.fragments.isEmpty()) {
                if (it.isEmpty()) {
                    childFragmentManager.beginTransaction()
                        .add(R.id.messages_field, EmptyFragment()).commit()
                } else {
                    childFragmentManager.beginTransaction()
                        .add(R.id.messages_field, ChatListFragment.newInstance(it)).commit()
                }
            }else {
                if (it.isEmpty()) {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.messages_field, EmptyFragment()).commit()
                } else {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.messages_field, ChatListFragment.newInstance(it)).commit()
                }
            }
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
}