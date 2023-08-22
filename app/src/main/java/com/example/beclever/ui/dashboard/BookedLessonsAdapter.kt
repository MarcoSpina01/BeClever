package com.example.beclever.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.databinding.ItemBookedlessonsBinding
import com.example.beclever.ui.plus.Lesson

class BookedLessonsAdapter(emptyList: List<Any>) : RecyclerView.Adapter<BookedLessonsAdapter.ViewHolder>() {

    private var lessons: List<Lesson> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookedlessonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.bind(lesson)
    }

    override fun getItemCount(): Int = lessons.size

    fun submitList(lessons: List<Lesson>) {
        this.lessons = lessons
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemBookedlessonsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.bookedlesson = lesson
            binding.executePendingBindings()
        }
    }
}







