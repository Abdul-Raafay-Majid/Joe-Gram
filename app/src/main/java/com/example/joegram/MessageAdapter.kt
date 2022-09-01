package com.example.joegram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context,val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ID_SENT=1
    val ID_RECEIVED=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            return SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false))
        } else{
            return ReceivedViewHolder(LayoutInflater.from(context).inflate(R.layout.received_layout, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid==currMessage.senderId){ return ID_SENT}
        else{return ID_RECEIVED}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage=messageList[position].message
        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
            holder.sentMessage.text=currMessage
        } else{
            val viewHolder=holder as ReceivedViewHolder
            holder.receivedMessage.text=currMessage
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.txt_view_sent)
    }
    class ReceivedViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val receivedMessage=itemView.findViewById<TextView>(R.id.txt_view_received)
    }
}