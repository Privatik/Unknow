package com.io.unknow.ui.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.io.unknow.R
import com.io.unknow.adapter.AdapterGallery
import com.io.unknow.databinding.ActivityDialogBinding
import com.io.unknow.model.Chat
import com.io.unknow.model.IMessage
import com.io.unknow.navigation.*
import com.io.unknow.parse.CameraParse
import com.io.unknow.ui.fragment.DialogWithUserFragment
import com.io.unknow.util.Setting
import com.io.unknow.viewmodel.activity.DialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


private const val REQUEST_TAKE_PHOTO = 1
private const val GALLERY = "Gallery"
class DialogActivity : AppCompatActivity(), IBottomSheet, IRedactModeActivity,
    IDialogRedactMessage {

    private lateinit var binding: ActivityDialogBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val cameraParse by lazy{ CameraParse(this)}

    private lateinit var loadImageFromGallery: ILoadImageFromGallery
    private lateinit var redactModeFragment: IRedactModeFragment
    private lateinit var dialogRedactMessage: IDialogRedactMessage
    private lateinit var sendImages: ImageButton
    private var isNotRedact: Boolean = true

    var imagesSelected: MutableList<String> = mutableListOf()

    companion object{
        private const val CHAT = "chat"
        private const val USER_ID = "userId"

        fun newInstance(context: Context,chat: Chat?, userId: String) = Intent(context,DialogActivity::class.java).apply {
            putExtra(CHAT, chat)
            putExtra(USER_ID, userId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_dialog)
        setContentView(binding.root)

        binding.viewModel = ViewModelProvider(this).get(DialogViewModel::class.java)


        try {
            val userId =  intent.getStringExtra(USER_ID)!!
            val chat = intent.getSerializableExtra(CHAT)?.let { it as Chat }
            if (chat == null) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.viewModel?.getChat(userId)?.collect {
                        showDialogUserFragment(chat = it, userId = userId)
                    }
                }
            }
            else {
                showDialogUserFragment(chat = chat, userId = userId)
            }
        }catch (e: Exception){

        }

        bottomSheetBehavior = from(binding.bottomSheet.bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.i("Bottom", "$slideOffset ${binding.fragmentContent.y} ${slideOffset * 100}")

                binding.fragmentContent.translationY = -slideOffset * binding.content.height
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.e("Close","$newState")
                if (newState == 4){
                    Log.e("Close","changeImage")
                    changeImageButtonOnSendText()
                }
            }

        })
        bottomSheetBehavior.state = STATE_HIDDEN

        binding.bottomSheet.camera.setOnClickListener {
            loadCamera()
        }


        binding.viewModel!!.liveDataOnline.observeForever {
            binding.status.text = if (it) "online" else "offline"
        }


        binding.back.setOnClickListener { onBackPressed() }

    }

    private fun showDialogUserFragment(chat: Chat, userId: String){
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_content,
            DialogWithUserFragment.newInstance(chat = chat, userId = userId)
        ).commit()

        binding.name.text = userId
        binding.viewModel?.online(userId = userId)
    }

    private fun loadCamera() {
        try {
            startActivityForResult(cameraParse.dispatchTakePictureIntent(), REQUEST_TAKE_PHOTO)
        } catch (e: ActivityNotFoundException) {
            Log.i("CAMERA","${e.message}")
        }catch (e: Exception){
            Log.i("CAMERA","${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            loadImageFromGallery.sendImage(cameraParse.currentPhotoPath)
        } else {
            Toast.makeText(this,"Ошибка загрузки", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NewApi")
    override fun loadBottomSheet() {
        if (!binding.viewModel!!.isPhotoLoad) {
            binding.bottomSheet.gallery.layoutManager = GridLayoutManager(this, 3)

            binding.bottomSheet.gallery.adapter = AdapterGallery(context = this, list =  binding.viewModel!!.getCameraImages(this).reversed(),imagesSelected = binding.viewModel?.sparedFlow!!)

            CoroutineScope(Dispatchers.Main).launch{
                binding.viewModel?.sparedFlow!!.collect{
                    Log.i(GALLERY,"check url $it")

                    if (it in imagesSelected){ imagesSelected.remove(it) } else { imagesSelected.add(it) }
                }


            }

            binding.viewModel!!.isPhotoLoad = true
        }

        bottomSheetBehavior.state = STATE_HALF_EXPANDED
        sendImages.setImageResource(R.drawable.ic_baseline_photo_filter_24)
        loadImageFromGallery.isChangeBooleanSend(isChange = true)
    }

    private fun changeImageButtonOnSendText(){
        sendImages.setImageResource(R.drawable.ic_send)
        loadImageFromGallery.isChangeBooleanSend(isChange = false)
    }

    override fun closeBottomSheet() {
        bottomSheetBehavior.state = STATE_HIDDEN
        changeImageButtonOnSendText()
        imagesSelected.clear()
    }


    override fun loadInterfaceForLoadImages(
        loadImageFromGallery: ILoadImageFromGallery,
        sendButton: ImageButton,
    ) {
        this.loadImageFromGallery = loadImageFromGallery
        this.sendImages = sendButton
    }

    override fun onBackPressed() {
        when {
            !isNotRedact -> {
                redactModeOff()
                Log.e("Exit","notRedact")
            }
            bottomSheetBehavior.state != STATE_HIDDEN -> {
                closeBottomSheet()
                Log.e("Exit","closeBottom ${bottomSheetBehavior.state} == $STATE_HIDDEN result: ${ bottomSheetBehavior.state != STATE_HIDDEN}")
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun redactModeOn() {
        isNotRedact = false
    }

    override fun redactModeOff() {
        isNotRedact = true
        redactModeFragment.editModeClose()
    }

    override fun sendRedactModeFragment(redactModeFragment: IRedactModeFragment, dialogRedactMessage: IDialogRedactMessage) {
        this.redactModeFragment = redactModeFragment
        this.dialogRedactMessage = dialogRedactMessage
    }

    override fun isRedactOff(): Boolean = isNotRedact

    override fun edit(message: IMessage) {
        dialogRedactMessage.edit(message = message)
    }

    override fun delete(message: IMessage, isDeleteForAll: Boolean) {
        dialogRedactMessage.delete(message = message,isDeleteForAll = isDeleteForAll)
    }


}