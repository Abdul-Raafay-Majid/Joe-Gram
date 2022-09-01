package com.example.joegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var recycler_view_chat:RecyclerView
    private lateinit var edt_txt_message:EditText
    private lateinit var btn_send:ImageButton
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var DbRef:DatabaseReference
    var receiverRoom:String?=null
    var senderRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Initializing
        recycler_view_chat=findViewById(R.id.recycler_view_chat)
        edt_txt_message=findViewById(R.id.edt_txt_message)
        btn_send=findViewById(R.id.btn_send)
        messageList= ArrayList()
        messageAdapter=MessageAdapter(this,messageList)
        DbRef=FirebaseDatabase.getInstance().getReference()

        val layout_manager=LinearLayoutManager(this)
        layout_manager.reverseLayout
        recycler_view_chat.layoutManager=layout_manager
        recycler_view_chat.adapter=messageAdapter
        val name=intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")

        supportActionBar?.title=name?.uppercase()
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid

        //adding messages to recycler view
        DbRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val currMessage=postSnapshot.getValue(Message::class.java)
                        messageList.add(currMessage!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    layout_manager.scrollToPosition(messageList.size-1)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        //On Click Listeners
        btn_send.setOnClickListener {
            val message=edt_txt_message.text.toString()
            val messageObject=Message(message,senderUid)
            DbRef.child("Chats").child(senderRoom!!).child("messages").push().setValue(messageObject)
                .addOnSuccessListener { DbRef.child("Chats").child(receiverRoom!!).child("messages").push().setValue(messageObject) }
            edt_txt_message.setText("")
        }

        edt_txt_message.setOnClickListener { layout_manager.scrollToPosition(messageList.size-1) }




    }

    private fun messageInList(message:Message):Boolean {
        messageList.forEach { item ->
            if (message == item) {
                return true }
        }
        return false
    }
}