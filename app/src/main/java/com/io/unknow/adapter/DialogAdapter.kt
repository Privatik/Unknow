package com.io.unknow.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.unknow.R
import com.io.unknow.databinding.MyMessageBinding
import com.io.unknow.databinding.UserMessageBinding
import com.io.unknow.model.Message
import com.io.unknow.model.TypeMessage
import com.io.unknow.parse.CalendarParse
import com.io.unknow.ui.dialogfragment.EditMessageDialogFragment
import com.io.unknow.util.ToastMessage

class DialogAdapter(private val context: Context, private val list: List<Message>, private val userId: String, private val fragmentManager: FragmentManager): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "DialogAdapter"
    private var isNotLoad = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        Log.i(TAG, "draw onCreateViewHolder ${viewType}")
        if (viewType == 1){
            return MyMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.my_message,parent,false),fragmentManager)
        }
        return UserMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false),fragmentManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder $position")
        if (isNotLoad && list.lastIndex == position){
            ToastMessage.topMessage(context,list[position].time.substring(8,10) + CalendarParse.getMounth(list[position].time.substring(5,8)))
            isNotLoad = false
            Log.i("Rec","View one toast")
        }

        val message = list[position]

       if (holder is MyMessageItem){
           holder.myMessageBinding.message = message

           if (message.type == TypeMessage.TEXT){
               holder.bindText(message.time.substring(11,16))
           }else{
               holder.bindImage(message.imageUrl,context)
           }



       }else if (holder is UserMessageItem){
          holder.userMessageBinding.message = message

           if (message.type == TypeMessage.TEXT){
               holder.bindText(message.time.substring(11,16))
           }else{
               holder.bindImage(message.imageUrl,context)
           }
       }
    }

    fun getMessage(position: Int):Message = list[position]

    override fun getItemViewType(position: Int): Int {
        if (list[position].userId == userId){
            return 0
        }
        return 1
    }

    override fun getItemCount(): Int = list.size


    class MyMessageItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView), IBindMessage{
       val myMessageBinding = MyMessageBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(false).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }

        override fun bindText(time: String){
            if (myMessageBinding.time.visibility == View.GONE){
                myMessageBinding.time.visibility = View.VISIBLE
                myMessageBinding.image.visibility = View.GONE
            }

            myMessageBinding.time.text = time
        }

        override fun bindImage(imageUrl: String, context: Context){
            if (myMessageBinding.image.visibility == View.GONE){
                myMessageBinding.image.visibility = View.VISIBLE
                myMessageBinding.text.visibility = View.GONE
            }

            Glide.with(context)
                .load(imageUrl)
                .into( myMessageBinding.image)
        }
    }

    class UserMessageItem(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView), IBindMessage{
        val userMessageBinding = UserMessageBinding.bind(itemView)

        init {
            itemView.setOnLongClickListener{
                EditMessageDialogFragment.newInstance(true).show(fragmentManager,"edit")
                return@setOnLongClickListener true
            }
        }

        override fun bindText(time: String){
            if (userMessageBinding.time.visibility == View.GONE){
                userMessageBinding.time.visibility = View.VISIBLE
                userMessageBinding.image.visibility = View.GONE
            }

            userMessageBinding.time.text = time
        }

        override fun bindImage(imageUrl: String, context: Context) {
            if (userMessageBinding.image.visibility == View.GONE){
                userMessageBinding.image.visibility = View.VISIBLE
                userMessageBinding.text.visibility = View.GONE
            }

            Glide.with(context)
                .load(imageUrl)
                .into( userMessageBinding.image)
        }
    }

    interface IBindMessage{
        fun bindText(time: String)
        fun bindImage(imageUrl: String, context: Context)
    }

}