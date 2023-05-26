package com.example.gymapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.databinding.ActivityWorkoutsListBinding
import com.example.gymapp.recyclerViewAdapter.ItemAdapter

class WorkoutsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityWorkoutsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize data.
        val workoutsDataset = AppDataBase(this).getWorkoutsList()
        println(workoutsDataset.toString())

        val recyclerView = binding.workoutsList
        recyclerView.adapter = ItemAdapter(this, workoutsDataset)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}