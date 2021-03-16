package com.io.unknow.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.unknow.R
import com.io.unknow.databinding.*
import com.io.unknow.decoration.SectionCallback
import com.io.unknow.model.IMessage
import com.io.unknow.model.MessageDate
import com.io.unknow.model.MessageImage
import com.io.unknow.model.MessageText
import com.io.unknow.navigation.IBindImage
import com.io.unknow.navigation.IBindText
import com.io.unknow.ui.dialogfragment.EditMessageDialogFragment


private const val TAG = "DialogAdapter"
class DialogAdapter(private val context: Context, private val list: List<IMessage>, private val userId: String, private val fragmentManager: FragmentManager): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    SectionCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType){
            -1 -> MyMessageTextItem(LayoutInflater.from(parent.context).inflate(R.layout.my_message_text,parent,false),fragmentManager)
            -2 -> MyMessageImageItem(LayoutInflater.from(parent.context).inflate(R.layout.my_message_image,parent,false),fragmentManager)
            1 -> UserMessageTextItem(LayoutInflater.from(parent.context).inflate(R.layout.user_message_text,parent,false),fragmentManager)
            2 -> UserMessageImageItem(LayoutInflater.from(parent.context).inflate(R.layout.user_massage_image,parent,false),fragmentManager)
            else -> DateItem(LayoutInflater.from(parent.context).inflate(R.layout.item_data_messages,parent,false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder $position")
        val message = list[position]

        when (holder) {
            is DateItem -> {
                holder.dateBinding.message = message as MessageDate
            }
            is MyMessageTextItem -> {
                holder.myMessageBinding.message = message as MessageText
                holder.bindText()
            }
            is UserMessageTextItem -> {
                holder.userMessageBinding.message = message as MessageText
                holder.bindText()
            }
            is MyMessageImageItem -> {
                holder.myMessageBinding.message = message as MessageImage
                holder.bindImage(context = context)
            }
            is UserMessageImageItem -> {
                holder.userMessageBinding.message = message as MessageImage
                holder.bindImage(context = context)
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (list[position].userId == "") return 0
        else if (list[position].userId == userId){
            return if (list[position] is MessageText) 1 else 2
        }
        return if (list[position] is MessageText) -1 else -2
    }

    override fun getItemCount(): Int = list.size


    class MyMessageTextItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView), IBindText{
       val myMessageBinding = MyMessageTextBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(false).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }

        override fun bindText(){
            myMessageBinding.time.text = myMessageBinding.message?.time!!.substring(0,10)
        }
    }

    class UserMessageTextItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView), IBindText {
        val userMessageBinding = UserMessageTextBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(true).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }

        override fun bindText(){
            userMessageBinding.time.text = userMessageBinding.message?.time!!.substring(0,10)
        }

    }

    class MyMessageImageItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView), IBindImage{
        val myMessageBinding = MyMessageImageBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(false).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }


        override fun bindImage(context: Context){
            Glide.with(context)
                .load(myMessageBinding.message?.uri)
                .into(myMessageBinding.image)
        }
    }

    class UserMessageImageItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView),
        IBindImage {
        val userMessageBinding = UserMassageImageBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(true).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }

        override fun bindImage(context: Context) {
            Glide.with(context)
                .load(userMessageBinding.message?.uri)
                .into(userMessageBinding.image)
        }
    }

    class DateItem(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dateBinding = ItemDataMessagesBinding.bind(itemView)
    }


    override fun getHeaderPositionForItem(itemPosition: Int): Int =
        (itemPosition downTo 0)
            .map { Pair(isHeader(it), it) }
            .firstOrNull { it.first }?.second ?: RecyclerView.NO_POSITION

    override fun getHeaderLayout(headerPosition: Int): Int = R.layout.item_data_messages

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val text = header.findViewById<TextView>(R.id.data)
        text.text = list[headerPosition].time
    }

    override fun isHeader(itemPosition: Int): Boolean = list[itemPosition] is MessageDate

}