package com.example.todolist

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<Task> ): RecyclerView.Adapter<TaskAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.TaskTextView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = tasks[position].task

        viewHolder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, TaskDetails::class.java)

            intent.putExtra("pos", position)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }


}