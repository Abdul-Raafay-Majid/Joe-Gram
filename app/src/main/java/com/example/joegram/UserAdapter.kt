package com.example.joegram

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context, val Users:ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_view_user_icon=itemView.findViewById<TextView>(R.id.txt_view_user_icon)
        val txt_view_user_name=itemView.findViewById<TextView>(R.id.txt_view_user_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currUser=Users[position]
        val userName=currUser.name
        val firstChar=userName!!.first().toString().uppercase()
        holder.txt_view_user_icon.text= firstChar
        holder.txt_view_user_name.text=userName

        holder.itemView.setOnClickListener {
            val intent= Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currUser.name)
            intent.putExtra("uid",currUser.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return Users.size
    }
}