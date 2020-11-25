package com.io.unknow.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.io.unknow.R
import com.io.unknow.model.User
import com.io.unknow.viewmodel.fragment.ProfileViewModel
import javax.inject.Inject


class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val id = view.findViewById<TextView>(R.id.id)
        val sex = view.findViewById<TextView>(R.id.sex)
        val age = view.findViewById<TextView>(R.id.age)


        viewModel.liveData.observeForever {user ->
            id.text = "Id: ${user.id}"
            sex.text = "Пол: ${user.sex}"
            age.text = "Возраст: ${user.age.toString()}"

        }

        return view
    }

}