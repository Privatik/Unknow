package com.io.unknow.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.databinding.FragmentProfileBinding
import com.io.unknow.navigation.IMainExit
import com.io.unknow.navigation.IScrooll
import com.io.unknow.ui.dialogfragment.ProfileUpdateDialogFragment
import com.io.unknow.navigation.IUpdateUser
import com.io.unknow.ui.dialogfragment.SettingDialogFragment
import com.io.unknow.viewmodel.fragment.ProfileViewModel


class ProfileFragment : Fragment(), IUpdateUser {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainExit: IMainExit
    private lateinit var scrooll: IScrooll

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainExit = context as IMainExit
        scrooll = context as IScrooll
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel!!.liveData.observeForever { user ->
            if (user != null) {
                if (user.isBan){
                    binding.exitBan.visibility = View.VISIBLE
                    binding.ban.visibility = View.VISIBLE
                    binding.loadingProgressBar.visibility = View.GONE
                    scrooll.isScrollPager(false)
                    return@observeForever
                }
                Log.i("User",user.toString())
                binding.field.visibility = View.VISIBLE
                binding.loadingProgressBar.visibility = View.GONE

                binding.viewmodel?.updateUser()
                binding.editAge.text = binding.viewmodel?.ageString()
                binding.editSex.text = binding.viewmodel?.sexString()
                binding.id.text =
                    "Id: ${binding.viewmodel?.user!!.id}"
                if (binding.viewmodel?.user!!.height != null) {
                    binding.editHeight.text = binding.viewmodel?.user!!.height.toString() + " см"
                }
                if (binding.viewmodel?.user!!.weight != null) {
                    binding.editWeight.text = binding.viewmodel?.user!!.weight.toString() + " кг"
                }
            }
        }

        binding.setting.setOnClickListener {
            SettingDialogFragment().show(childFragmentManager,"setting")
        }

        binding.exit.setOnClickListener {
            binding.viewmodel?.exit()

            mainExit.exit()
        }

        binding.exitBan.setOnClickListener {
            binding.viewmodel?.exit()

            mainExit.exit()
        }

        binding.update.setOnClickListener {
            ProfileUpdateDialogFragment.newInstance(this, binding.editHeight.text.toString().replace(" см",""),binding.editWeight.text.toString().replace(" кг","")).show(childFragmentManager,"update")
        }

    }

    override fun update(height: String, weight: String) {
        if (height == "") {
            binding.viewmodel?.user!!.height = null
            binding.editHeight.text = ""
        }
        else{
            binding.viewmodel?.user!!.height = height.toInt()
            binding.editHeight.text = "$height см"
        }
        if (weight == ""){
            binding.viewmodel?.user!!.weight = null
            binding.editWeight.text = ""
        }
        else {
            binding.viewmodel?.user!!.weight = weight.toInt()
            binding.editWeight.text = "$weight кг"
        }
        binding.viewmodel?.updateUserBase()
    }
}