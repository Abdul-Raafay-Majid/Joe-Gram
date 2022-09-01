package com.example.joegram

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    //View Elements Declaration
    private lateinit var edt_txt_email: EditText
    private lateinit var edt_txt_pass: EditText
    private lateinit var btn_login: Button
    private lateinit var btn_signup: Button

    //FireBase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        //Initializing Views
        edt_txt_email = findViewById(R.id.edt_txt_email_login)
        edt_txt_pass = findViewById(R.id.edt_txt_pass_login)
        btn_login = findViewById(R.id.btn_login_login)
        btn_signup = findViewById(R.id.btn_signup_login)
        mAuth = FirebaseAuth.getInstance()

        //On Click Listeners
        btn_signup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val email = edt_txt_email.text.toString()
            val password = edt_txt_pass.text.toString()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
