package com.io.unknow.navigation

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.io.unknow.databinding.*
import com.io.unknow.glide.GlideApp
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.ui.dialogfragment.EditMessageDialogFragment

abstract class MessageViewHolder(itemView: View, private val fragmentManager: FragmentManager, private val isRedactMode: Boolean): RecyclerView.ViewHolder(itemView){

    init {
        itemView.setOnLongClickListener{
            if (isRedactMode) {
                EditMessageDialogFragment.newInstance(
                    false,
                    getMessage()
                ).show(fragmentManager, "edit")

                Log.i("TypeMessage","${getMessage()}")
            }
            return@setOnLongClickListener true
        }
    }

    abstract fun getMessage(): IMessage
}

class MyMessageTextItem(itemView: View, fragmentManager: FragmentManager, isRedactMode: Boolean): MessageViewHolder(itemView, fragmentManager, isRedactMode), IBindText{
    val myMessageBinding = MyMessageTextBinding.bind(itemView)


    override fun bindText(){
        myMessageBinding.time.text = myMessageBinding.message?.time!!.substring(11)
    }

    override fun getMessage(): IMessage = myMessageBinding.message as MessageText
}

class UserMessageTextItem(itemView: View, fragmentManager: FragmentManager, isRedactMode: Boolean) : MessageViewHolder(itemView, fragmentManager, isRedactMode), IBindText {
    val userMessageBinding = UserMessageTextBinding.bind(itemView)

    override fun bindText(){
        userMessageBinding.time.text = userMessageBinding.message?.time!!.substring(11)
    }

   override fun getMessage(): IMessage = userMessageBinding.message as MessageText

}

class MyMessageImageItem(itemView: View, fragmentManager: FragmentManager, isRedactMode: Boolean) : MessageViewHolder(itemView, fragmentManager, isRedactMode), IBindImage{
    val myMessageBinding = MyMessageImageBinding.bind(itemView)

    override fun bindImage(context: Context){
        Log.i("Glide","url:  ${myMessageBinding.message?.imageUrl}")

        Glide.with(context)
            .load(myMessageBinding.message?.imageUrl)
            .into(myMessageBinding.image)

        myMessageBinding.time.text = myMessageBinding.message?.time!!.substring(11)
    }

    override fun getMessage(): IMessage = myMessageBinding.message as MessageImage
}

class UserMessageImageItem(itemView: View, fragmentManager: FragmentManager, isRedactMode: Boolean): MessageViewHolder(itemView, fragmentManager, isRedactMode),
    IBindImage {
    val userMessageBinding = UserMassageImageBinding.bind(itemView)

    override fun bindImage(context: Context) {
        Glide.with(context)
            .load(userMessageBinding.message?.imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.i("Glide","${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

            })
            .into(userMessageBinding.image)

        userMessageBinding.time.text = userMessageBinding.message?.time!!.substring(11)
    }

    override fun getMessage(): IMessage = userMessageBinding.message as MessageImage
}

class DateItem(itemView: View) : RecyclerView.ViewHolder(itemView){
    val dateBinding = ItemDataMessagesBinding.bind(itemView)
}