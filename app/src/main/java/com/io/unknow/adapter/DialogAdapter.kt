package com.io.unknow.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io.unknow.R
import com.io.unknow.databinding.MyMessageBinding
import com.io.unknow.databinding.UserMessageBinding
import com.io.unknow.model.Message
import com.io.unknow.parse.CalendarParse
import com.io.unknow.util.ToastMessage

class DialogAdapter(private val context: Context, private val list: List<Message>, private val userId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var date: String = ""
    private val TAG = "DialogAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        Log.i(TAG, "draw onCreateViewHolder ${viewType}")
        if (viewType == 1){
            return MyMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.my_message,parent,false))
        }
        return UserMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder $position")
        if (list[position].time.substring(0,10) != date){
            date = list[position].time.substring(0,10)
            ToastMessage.topMessage(context,list[position].time.substring(8,10) + CalendarParse.getMounth(list[position].time.substring(5,8)))
        }
       if (holder is MyMessageItem){
          holder.myMessageBinding.message = list[position]
           holder.bind(list[position].time.substring(11,16))
       }else if (holder is UserMessageItem){
          holder.userMessageBinding.message = list[position]
           holder.bind(list[position].time.substring(11,16))
       }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].userId == userId){
            return 0
        }
        return 1
    }

    override fun getItemCount(): Int = list.size


    class MyMessageItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val myMessageBinding = MyMessageBinding.bind(itemView)

        fun bind(time: String){
            myMessageBinding.time.text = time
        }
    }

    class UserMessageItem(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userMessageBinding = UserMessageBinding.bind(itemView)

        fun bind(time: String){
            userMessageBinding.time.text = time
        }
    }

}