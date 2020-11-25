package com.io.unknow.adapter

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

class DialogAdapter(private val list: List<Message>, private val userId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        Log.i("AdapterDialog", list.size.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        Log.i("AdapterDialog", "draw ${viewType}")
        if (viewType == 1){
            return MyMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.my_message,parent,false))
        }
        return UserMessageItem(LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is MyMessageItem){
          holder.myMessageBinding.message = list[position]
       }else if (holder is UserMessageItem){
          holder.userMessageBinding.message = list[position]
       }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].userId == userId){
            return 1
        }
        return 0
    }

    override fun getItemCount(): Int = list.size


    class MyMessageItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val myMessageBinding = MyMessageBinding.bind(itemView)

    }

    class UserMessageItem(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userMessageBinding = UserMessageBinding.bind(itemView)
    }

}