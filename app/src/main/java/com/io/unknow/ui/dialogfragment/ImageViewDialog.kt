package com.io.unknow.ui.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.io.unknow.R
import com.ortiz.touchview.TouchImageView

class ImageViewDialog: DialogFragment() {

    companion object{
        private const val TAG_IMAGE = "image"

        fun newInstance(imageUri: String):ImageViewDialog {
            val args = Bundle()
            args.putString(TAG_IMAGE,imageUri)

            val fragment = ImageViewDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_image_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val image = view.findViewById<TouchImageView>(R.id.image)

        Log.e("LoadImage","${arguments?.getString(TAG_IMAGE)}")

        Glide.with(this)
            .load(arguments?.getString(TAG_IMAGE))
            .into(image)
    }
}