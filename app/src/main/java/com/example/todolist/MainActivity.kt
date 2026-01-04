package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var tasks: List<Task> = mutableListOf()
    private lateinit var signout: Button
    private lateinit var newTask: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            // Not signed in, redirect to LoginActivity
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
            return
        }
        Log.d("WHOISSIGNEDIN", "signed in with ${auth.uid}")
        db = FirebaseFirestore.getInstance()

        signout = findViewById(R.id.SignOutButton)
        newTask = findViewById(R.id.AddButton)
        recyclerView = findViewById(R.id.TasksView)
        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }
        newTask.setOnClickListener {
            //todo
            val intent = Intent(this, NewTask::class.java)
            startActivity(intent)
            finish()

            //db.collection("users")
            //    .document(auth.currentUser?.uid ?: return@setOnClickListener)
            //    .collection("tasks")
            //    .add(hashMapOf("task" to "add task works"))

        }

    }

    override fun onResume() {
        super.onResume()
        // fetch tasks from database
        showTasks()
    }

    fun showTasks() {
        tasks = mutableListOf()
        val currentUser = auth.currentUser
        val currentID = currentUser?.uid ?: return
        db.collection("users")
            .document(currentID)
            .collection("tasks")
            .get()
            .addOnSuccessListener { t ->
                t.documents.forEach { s ->
                    tasks+=Task((s.data?.get("task") ?: "") as String)
                }
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = TaskAdapter(tasks)
                recyclerView.adapter?.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Toast.makeText(baseContext, "Failed getting tasks: ${e}", Toast.LENGTH_LONG)
            }

    }



}