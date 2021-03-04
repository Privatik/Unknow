package com.io.unknow.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.io.unknow.R
import com.io.unknow.adapter.AdapterGallery
import com.io.unknow.databinding.ActivityDialogBinding
import com.io.unknow.model.Chat
import com.io.unknow.navigation.IBottomSheet
import com.io.unknow.ui.dialogfragment.DialogWithUserFragment
import com.io.unknow.util.Setting
import com.io.unknow.viewmodel.activity.DialogViewModel
import com.io.unknow.viewmodel.activity.LoginViewModel

private const val REQUEST_TAKE_PHOTO = 1
class DialogActivity : AppCompatActivity(), IBottomSheet {

    private lateinit var binding: ActivityDialogBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var imagesSelected: MutableList<String> = mutableListOf()

    companion object{
        private const val CHAT = "chat"
        private const val USER_ID = "userId"

        fun newInstance(context: Context,chat: Chat, userId: String):Intent = Intent(context,DialogActivity::class.java)
            .putExtra(CHAT,chat)
            .putExtra(USER_ID,userId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_dialog)
        setContentView(binding.root)

        binding.viewModel = ViewModelProvider(this).get(DialogViewModel::class.java)


        try {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_content, DialogWithUserFragment.newInstance(chat = intent.getSerializableExtra(CHAT) as Chat,userId = intent.getStringExtra(USER_ID)!!)).commit()
        }catch (e: Exception){

        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)

        binding.bottomSheet.camera.setOnClickListener {
            loadCamera()
        }
    }

    private fun loadCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when(requestCode){
            REQUEST_TAKE_PHOTO ->{
                if(resultCode == RESULT_OK && data !== null){
                 //   imageView.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun loadBottomSheet() {
        if (!binding.viewModel!!.isPhotoLoad) {
            binding.bottomSheet.gallery.layoutManager = GridLayoutManager(this, 3)
            binding.bottomSheet.gallery.adapter =
                AdapterGallery(this, binding.viewModel!!.getCameraImages(this).reversed(), imagesSelected)

            binding.viewModel!!.isPhotoLoad = true
        }

        bottomSheetBehavior.peekHeight = binding.fragmentContent.height / 2
    }

    override fun closeBottomSheet() {
        bottomSheetBehavior.peekHeight = 0
        imagesSelected.clear()
    }

    override fun loadImages(): List<String> {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.peekHeight != 0){
            closeBottomSheet()
        }
        else{
            super.onBackPressed()
        }
    }


}