package com.example.joegram

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    //View Elements Declaration
    private lateinit var edt_txt_user: EditText
    private lateinit var edt_txt_email: EditText
    private lateinit var edt_txt_pass: EditText
    private lateinit var btn_signup: Button
    private lateinit var DbRef: DatabaseReference
    //FireBase Auth
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        //Initializing Views
        edt_txt_user=findViewById(R.id.edt_txt_user_signup)
        edt_txt_email=findViewById(R.id.edt_txt_email_signup)
        edt_txt_pass=findViewById(R.id.edt_txt_pass_signup)
        btn_signup=findViewById(R.id.btn_signup_signup)
        mAuth=FirebaseAuth.getInstance()

        //On Click Listeners
        btn_signup.setOnClickListener {
            val name=edt_txt_user.text.toString()
            val email=edt_txt_email.text.toString()
            val password=edt_txt_pass.text.toString()
            signUp(name,email,password)
        }
    }

    private fun signUp(name:String,email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                   val intent= Intent(this@SignUp,MainActivity::class.java)
                    startActivity(intent)
                } else{
                    Toast.makeText(this@SignUp,"Some Error Ocurred",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name:String,email:String,uid:String){
        DbRef=FirebaseDatabase.getInstance().getReference()
        DbRef.child("User").child(uid).setValue(User(name,email,uid))

    }
}