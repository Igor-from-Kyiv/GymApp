package com.example.gymapp.recyclerViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.Workout
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ItemAdapter(private val context: Context, private val dataset: List<Workout>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val workoutDateView: TextView = view.findViewById(R.id.workout_date)
        val workoutTotalView: TextView = view.findViewById(R.id.workout_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.workout_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        var dateStr = ""
        val dateTime = item.date.toLocalDateTime(TimeZone.UTC)
        dateStr = "date: ${dateTime.date}"

        holder.workoutDateView.text = dateStr
        holder.workoutTotalView.text = "workout total: ${item.workoutTotal.toString()} kg"
    }
}