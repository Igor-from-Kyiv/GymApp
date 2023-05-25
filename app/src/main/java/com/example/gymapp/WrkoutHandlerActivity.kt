package com.example.gymapp

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapp.AppDataBase.Companion.SQUAT
import com.example.gymapp.databinding.ActivityWorkoutHandlerBinding


class WorkoutHandlerActivity : AppCompatActivity(), CreateExerciseDialog.InputDialogListener {

    lateinit var binding: ActivityWorkoutHandlerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workout = Workout()
        var currentExercise = SQUAT

        val addButton = binding.addExercise
        addButton.setOnClickListener {
            val weight = binding.weightInput.text.toString().toInt()
            binding.weightInput.setText("")
            val reps = binding.repsInput.text.toString().toInt()
            binding.repsInput.setText("")
            val exercise = Exercise(Tag(currentExercise), weight, reps)
            workout.addExercise(exercise)
        }

        val stopButton = binding.stopWorkout
        stopButton.setOnClickListener {
            AppDataBase(this).insertWorkout(workout)
        }

        // Select exercise section
        val select = binding.selectExercise
        val cursor = AppDataBase(this).fetchTags()
        val adapter = AppCursorAdapter(this, cursor, 0)

        select.adapter = adapter

        select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = view?.findViewById<TextView>(android.R.id.text1)?.text
                Toast.makeText(
                    this@WorkoutHandlerActivity,
                    "test position: $selectedOption, id: $id", Toast.LENGTH_SHORT
                ).show()
                currentExercise = selectedOption.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // show dialog popup on button click
        val createExerciseButton = binding.createExercise
        createExerciseButton.setOnClickListener {

            // Create dialog
            val inputDialog = AlertDialog.Builder(this)
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            inputDialog.setView(input)
            inputDialog.setTitle("Dialog Title")
            inputDialog.setMessage("Enter your input:")
            inputDialog.setPositiveButton("OK") { dialog, which ->
                val userInput = input.text.toString()
                val values = ContentValues()
                values.put("name", userInput)
                AppDataBase(this).insertTag(userInput)

                // Do further processing with the user input here
                val _cursor = AppDataBase(this).fetchTags()
                val c = adapter.swapCursor(_cursor)
                c.close()
                select.adapter = adapter
            }
            inputDialog.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                }
            )

            // Show the dialog
            inputDialog.show()
        }
    }
}
