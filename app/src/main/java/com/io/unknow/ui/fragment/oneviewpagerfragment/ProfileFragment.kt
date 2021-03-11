package com.io.unknow.ui.fragment.oneviewpagerfragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.databinding.FragmentProfileBinding
import com.io.unknow.navigation.*
import com.io.unknow.ui.dialogfragment.ProfileUpdateDialogFragment
import com.io.unknow.ui.dialogfragment.SettingDialogFragment
import com.io.unknow.util.Locate
import com.io.unknow.viewmodel.fragment.ProfileViewModel


private const val constructorTAG = "ProfileConstructor"
class ProfileFragment(val exit: IExit? = null) : Fragment(), IUpdateUser, IPushProfileFragment {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainExit: IMainExit
    private lateinit var updateActitivty: IUpdateActivity

    init {
        Log.i(constructorTAG,"init")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainExit = context as IMainExit
        updateActitivty  = context as IUpdateActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActitivty.update(this)

        try {
            Log.i("ProfileFragment", "onCreatedView ${arguments?.getInt("scroll")!!}")
            if (arguments?.getInt("scroll") == 0) throw KotlinNullPointerException("Y equals 0")
            binding.scrollview.viewTreeObserver.addOnGlobalLayoutListener (scrollListener)
        }catch (e: KotlinNullPointerException){
            Log.i("ProfileFragment", e.message)
        }

      if (CurrentUser.user != null) {
          binding.viewmodel?.user = CurrentUser.user!!
          binding.editAge.text = binding.viewmodel?.ageString()
          binding.editSex.text = binding.viewmodel?.sexString()
          binding.id.text =
              "Id: ${binding.viewmodel?.user!!.id}"
          if (binding.viewmodel?.user!!.height != null) {
              binding.editHeight.text =
                  binding.viewmodel?.user!!.height.toString() + Locate.getHeight()
          }
          if (binding.viewmodel?.user!!.weight != null) {
              binding.editWeight.text =
                  binding.viewmodel?.user!!.weight.toString() + Locate.getWeight()
          }
      }


        binding.setting.setOnClickListener {
            SettingDialogFragment().show(childFragmentManager, "setting")
        }

        binding.update.setOnClickListener {
            ProfileUpdateDialogFragment.newInstance(
                this, binding.editHeight.text.toString().replace(Locate.getHeight(), ""
                ), binding.editWeight.text.toString().replace(Locate.getWeight(), "")
            ).show(childFragmentManager, "update")
        }

        binding.exit.setOnClickListener {
            exit?.exit()
            mainExit.exit()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun update(height: String, weight: String) {
        if (height == "") {
            binding.viewmodel?.user!!.height = null
            binding.editHeight.text = ""
        }
        else{
            binding.viewmodel?.user!!.height = height.toInt()
            binding.editHeight.text = "$height ${Locate.getHeight()}"
        }
        if (weight == ""){
            binding.viewmodel?.user!!.weight = null
            binding.editWeight.text = ""
        }
        else {
            binding.viewmodel?.user!!.weight = weight.toInt()
            binding.editWeight.text = "$weight ${Locate.getWeight()}"
        }
        binding.viewmodel?.updateUserBase()
    }

    override fun scrollViewGetY(): Int = binding.scrollview.scrollY

    val scrollListener = OnGlobalLayoutListener {
        binding.scrollview.post {
            binding.scrollview.scrollTo(
                0,
                arguments?.getInt("scroll")!!
            )
        }
    }
}