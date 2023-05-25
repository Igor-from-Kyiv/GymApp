package com.example.gymapp

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import java.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gymapp.AppDataBase.Companion.SQUAT
import com.example.gymapp.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


const val FOUR_WEEKS_MILLISEC: Long = 2_419_200_000 // milliseconds in 4 weeks
const val DAY_MILLISEC: Long = 86_400_000 // milliseconds in 1 day 1000 * 60 * 60 * 24
class MainActivity : AppCompatActivity() {
    var now = Clock.System.now().toEpochMilliseconds()
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var chartHelper: ChartHelper
        lateinit var lineChartHelper: LineChartHelper
        var selectedExercise = SQUAT
        var selectedDate: Long

        // Select exercise section
        val select = binding.selectChartExercise
        select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = view?.findViewById<TextView>(android.R.id.text1)?.text
                selectedExercise = selectedOption.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val cursor = AppDataBase(this).readableDatabase.rawQuery("SELECT * FROM TAGS", null)
        val adapter = AppCursorAdapter(this, cursor, 0)

        select.adapter = adapter
        select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = view?.findViewById<TextView>(android.R.id.text1)?.text
                selectedExercise = selectedOption.toString()
                val newData = AppDataBase(this@MainActivity).getWorkoutsListByDate(now - FOUR_WEEKS_MILLISEC, now)
                chartHelper.updateChartData(newData, selectedExercise)
                lineChartHelper.updateChartData(newData, selectedExercise)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // DATE PICKER SECTION
        val pickDateBtn = binding.dateBtn

        // adding click listener for button
        pickDateBtn.setOnClickListener {
            // getting the instance of calendar.
            val c = Calendar.getInstance()

            // getting day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // are creating a variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // setting date to text view.
//                    selectedDate = Calendar.getInstance()
                    val text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    val text2 = year.toString() + "-" + (monthOfYear + 1).toString().padStart(2, '0') + "-" + dayOfMonth.toString().padStart(2, '0') + "T00:00:00.000Z"
                    selectedDate = Instant.parse(text2).toEpochMilliseconds()

                    // get new list of workouts based on date input
                    // call redraw chart
                    val newData = AppDataBase(this@MainActivity).getWorkoutsListByDate(selectedDate, now)
                    chartHelper.updateChartData(newData, selectedExercise)
                    lineChartHelper.updateChartData(newData, selectedExercise)
                },
                // passing year, month And day for the selected date in date picker.
                year,
                month,
                day
            )
            // calling show to display date picker dialog.
            datePickerDialog.show()
        }

        val currentMonthWorkouts = AppDataBase(this).getWorkoutsList()
        now = Clock.System.now().toEpochMilliseconds()
        val currentMonthWorkouts2 = AppDataBase(this).getWorkoutsListByDate(now - FOUR_WEEKS_MILLISEC, now + DAY_MILLISEC)

        val barChart = binding.workoutBarChart
        barChart.setBackgroundColor(Color.BLUE)
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.xAxis.granularity = 1f
        barChart.axisLeft.setDrawZeroLine(true)
        barChart.axisLeft.zeroLineWidth = 2F
        barChart.axisLeft.zeroLineColor = Color.YELLOW
        val barxAxis = barChart.xAxis
        barxAxis.setDrawLabels(true)
        barxAxis.position = XAxis.XAxisPosition.BOTTOM
        barxAxis.valueFormatter = LabelFormatter()
        chartHelper = ChartHelper(barChart, currentMonthWorkouts2, selectedExercise)


        val lineChart = binding.workoutLineChart
        lineChart.setBackgroundColor(Color.YELLOW)
        lineChart.description.setEnabled(false)
        lineChart.axisLeft.setDrawZeroLine(true)
        lineChart.axisLeft.setZeroLineWidth(2f)
        lineChart.axisLeft.zeroLineColor = Color.BLUE
        lineChart.xAxis.granularity = 1f
        lineChartHelper = LineChartHelper(lineChart, currentMonthWorkouts2, selectedExercise)
        val linexAxis = lineChart.xAxis
        linexAxis.setDrawLabels(true)
        linexAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        linexAxis.valueFormatter = LabelFormatter()


        val barChartEntries = mutableListOf<BarEntry>()
        val lineChartEntries = mutableListOf<Entry>()

        val workoutsListButton = binding.workoutsList
        workoutsListButton.setOnClickListener {
            val intent = Intent(this, WorkoutsListActivity::class.java)
            startActivity(intent)
        }





        val startButton: Button = binding.startButton
        startButton.setOnClickListener {
            val intent = Intent(this, WorkoutHandlerActivity::class.java)
            startActivity(intent)
        }
    }
}
