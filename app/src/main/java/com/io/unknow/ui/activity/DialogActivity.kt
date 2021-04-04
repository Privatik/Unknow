package com.io.unknow.ui.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.io.unknow.util.TAGS.USER_ID
import com.io.unknow.viewmodel.activity.DialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


private const val REQUEST_TAKE_PHOTO = 1
private const val GALLERY = "Gallery"
class DialogActivity : AppCompatActivity(), IBottomSheet, IRedactModeActivity,
    IDialogRedactMessage<IMessage> {

    private lateinit var binding: ActivityDialogBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val cameraParse by lazy{ CameraParse(this)}

    private lateinit var loadImageFromGallery: ILoadImageFromGallery
    private lateinit var redactModeFragment: IRedactModeFragment
    private lateinit var dialogRedactMessage: IDialogRedactMessage<IMessage>
    private lateinit var sendImages: ImageButton
    private var isRedact: Boolean = false

    private var adapterGallery: AdapterGallery? = null

    var imagesSelected: MutableList<String> = mutableListOf()

    companion object{
        private const val CHAT = "chat"

        fun newInstance(context: Context,chat: Chat?, userId: String) = Intent(context,DialogActivity::class.java).apply {
            putExtra(CHAT, chat)
            putExtra(USER_ID, userId)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Setting.initActivity(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_dialog)
        setContentView(binding.root)

        binding.viewModel = ViewModelProvider(this).get(DialogViewModel::class.java)


        try {
            val userId =  intent.getStringExtra(USER_ID)!!
            val chat = intent.getParcelableExtra<Chat>(CHAT)
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


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

         updateGallery()
    }

    @SuppressLint("NewApi")
    fun updateGallery(){
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.bottomSheet.gallery.layoutManager = GridLayoutManager(this, 5)
        } else {
            binding.bottomSheet.gallery.layoutManager = GridLayoutManager(this, 3)
        }

        adapterGallery = AdapterGallery(context = this, list =  binding.viewModel!!.getCameraImages(this).reversed(),imagesSelected = binding.viewModel?.sparedFlow!!)
        binding.bottomSheet.gallery.adapter = adapterGallery!!

    }

    override fun loadBottomSheet() {
        if (!binding.viewModel!!.isPhotoLoad) {
            updateGallery()

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
        adapterGallery?.notifyDataSetChanged()
    }

    override fun isBottomShow(): Boolean = bottomSheetBehavior.state != STATE_HIDDEN


    override fun loadInterfaceForLoadImages(
        loadImageFromGallery: ILoadImageFromGallery,
        sendButton: ImageButton,
    ) {
        this.loadImageFromGallery = loadImageFromGallery
        this.sendImages = sendButton
    }

    override fun onBackPressed() {
        when {
            isRedact -> {
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
        isRedact = true
    }

    override fun redactModeOff() {
        isRedact = false
        redactModeFragment.editModeClose()
    }

    override fun sendRedactModeFragment(redactModeFragment: IRedactModeFragment, dialogRedactMessage: IDialogRedactMessage<IMessage>) {
        this.redactModeFragment = redactModeFragment
        this.dialogRedactMessage = dialogRedactMessage
    }

    override fun isRedactOff(): Boolean = !isRedact

    override fun edit(item: IMessage) {
        dialogRedactMessage.edit(item = item)
    }

    override fun delete(item: IMessage, isDeleteForAll: Boolean) {
        dialogRedactMessage.delete(item = item,isDeleteForAll = isDeleteForAll)
    }

    override fun delete(item: IMessage) {}


}