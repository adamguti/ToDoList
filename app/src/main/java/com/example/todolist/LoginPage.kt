package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        username = findViewById(R.id.UsernameEditText)
        password = findViewById(R.id.PasswordEditText)
        val login: Button = findViewById(R.id.LogInButton)
        val signup: Button = findViewById(R.id.NewAccountButton)


        login.setOnClickListener {
            login()
        }

        signup.setOnClickListener {
            signup()
        }


    }


    private fun login() {
        val user = username.text.toString()
        val pass = password.text.toString()

        auth.signInWithEmailAndPassword(user, pass)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(baseContext, "Incorrect Credentials: ${e.message}",
                    Toast.LENGTH_SHORT).show()
                username.setText("")
                password.setText("")
            }
    }

    private fun signup() {
        val user = username.text.toString()
        val pass = password.text.toString()

        if (user.isNotEmpty() && pass.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            auth.createUserWithEmailAndPassword(user, pass)
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "Account created for: ${user}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(baseContext, "Could not Create Account: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                    username.setText("")
                    password.setText("")
                }

        }
    }
}



