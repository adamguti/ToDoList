package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.plus

class TaskDetails : AppCompatActivity() {

    private var tasks: List<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val position = intent.getIntExtra("pos", 0)
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val textView: TextView = findViewById(R.id.ShowTaskTextView)

        val currentUser = auth.currentUser
        val currentID = currentUser?.uid ?: return
        db.collection("users")
            .document(currentID)
            .collection("tasks")
            .get()
            .addOnSuccessListener { t ->
                t.documents.forEach { s ->
                    Log.d("FUCKME2", "I succeeded!")
                    tasks+=Task((s.data?.get("task") ?: "") as String, (s.id))
                    Log.d("FUCKME", "tasks has ${tasks.size} length with contents of ${tasks}")
                }
                textView.setText(tasks[position].task)
            }.addOnFailureListener { e ->
                Toast.makeText(baseContext, "Failed getting tasks: ${e}", Toast.LENGTH_LONG)
                Log.d("FUCKME2", "I failed...")

            }

        val complete: Button = findViewById(R.id.MarkAsCompleteButton)
        complete.setOnClickListener {
            db.collection("users")
                .document(currentID)
                .collection("tasks")
                .document("${tasks[position].id}")
                .delete()
                .addOnSuccessListener { finish() }

        }

        val cancel: Button = findViewById(R.id.DontMarkAsCompleteButton)
        cancel.setOnClickListener {

            finish()
        }


    }

}