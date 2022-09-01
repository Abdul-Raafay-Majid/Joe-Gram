package com.example.joegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var users_recycler_view:RecyclerView
    private lateinit var Users:ArrayList<User>
    private lateinit var userAdapter:UserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var DbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.show()
        //Initializing
        Users= ArrayList()
        userAdapter= UserAdapter(this,Users)
        users_recycler_view=findViewById(R.id.users_recycler_view)
        mAuth=FirebaseAuth.getInstance()
        DbRef=FirebaseDatabase.getInstance().getReference()

        users_recycler_view.layoutManager=LinearLayoutManager(this)
        users_recycler_view.adapter=userAdapter

        DbRef.child("User").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (postSnapshot in snapshot.children){
                   val currUser=postSnapshot.getValue(User::class.java)
                   var isInList=userInList(currUser!!)
                   if(!isInList && mAuth.currentUser?.uid!=currUser.uid){
                       Users.add(currUser)
                       userAdapter.notifyItemInserted(Users.size-1)
                   }


               }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout-> {
            mAuth.signOut()
            val intent= Intent(this@MainActivity,Login::class.java)
            startActivity(intent)
            finish()
            return true
                }
        }
        return true
    }

    private fun userInList(user:User):Boolean {
        Users.forEach { item ->
            if (user.uid == item.uid) {
                return true }
        }
        return false
    }
}