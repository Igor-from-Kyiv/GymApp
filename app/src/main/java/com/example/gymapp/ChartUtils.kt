package com.example.gymapp

import android.graphics.Color
import com.example.gymapp.Workout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Date

class ChartHelper(val chart: BarChart, val data: List<Workout>, var label: String) {


    init{
        setNewLabel(label)
        updateChartData(data, label)
    }

    fun setNewLabel(newLabel: String) {
        this.label = newLabel
    }


    fun updateChartData (newData: List<Workout>, exercise: String) {
        setNewLabel(exercise)
        val workoutTotalEntries: MutableList<BarEntry> = mutableListOf()
        val exerciseTotalEntries: MutableList<BarEntry> = mutableListOf()
        val exerciseMaxLiftEntries: MutableList<Entry> = mutableListOf()
        var firstDate: Long? = null
        for (workout in newData) {
            val epochDay = workout.getEpochDay()

            var exerciseTotal = 0
            if (workout.exercisesDict.containsKey(exercise)) {
                if (firstDate == null) firstDate = epochDay
                workoutTotalEntries.add(BarEntry(epochDay.toFloat(), workout.workoutTotal.toFloat()))

                for (e in workout.exercisesDict[exercise]!!) {
                    exerciseTotal += e.weight * e.reps
                }

                exerciseTotalEntries.add(BarEntry(epochDay.toFloat(), floatArrayOf( exerciseTotal.toFloat()) ))
                if (workout.maxLifts[label] != null) {
                    exerciseMaxLiftEntries.add(Entry(epochDay.toFloat(), workout.maxLifts[label]!!.toFloat()))
                }
            }

        }

        val workoutTotalDataSet = BarDataSet(workoutTotalEntries, "workout total")
        val exerciseTotalDataSet = BarDataSet(exerciseTotalEntries, "$label total")
        exerciseTotalDataSet.color = Color.YELLOW
        val exerciseMaxLiftDataSet = LineDataSet(exerciseMaxLiftEntries, "$label max lift")
        exerciseMaxLiftDataSet.color = Color.RED
        exerciseMaxLiftDataSet.lineWidth = 3f
        chart.data = BarData(workoutTotalDataSet, exerciseTotalDataSet )
        val barData = chart.barData
//        chart.groupBars(firstDate!!.toFloat(), 0.4f, 0.1f )
        chart.invalidate()
    }
}


class LineChartHelper(val chart: LineChart, val data: List<Workout>, var label: String) {


    init{
        setNewLabel(label)
        updateChartData(data, label)
    }

    fun setNewLabel(newLabel: String) {
        this.label = newLabel
    }


    fun updateChartData (newData: List<Workout>, exercise: String) {
        setNewLabel(exercise)
        val exerciseMaxLiftEntries: MutableList<Entry> = mutableListOf()
        var firstDate: Long? = null
        for (workout in newData) {
            val epochDay = workout.getEpochDay()

            var exerciseTotal = 0
            if (workout.exercisesDict.containsKey(exercise)) {
                if (firstDate == null) firstDate = epochDay

                for (e in workout.exercisesDict[exercise]!!) {
                    exerciseTotal += e.weight * e.reps
                }

                if (workout.maxLifts[label] != null) {
                    exerciseMaxLiftEntries.add(Entry(epochDay.toFloat(), workout.getMaxLift(exercise).toFloat()))
                }
            }
        }

        val exerciseMaxLiftDataSet = LineDataSet(exerciseMaxLiftEntries, "$label max lift")
        exerciseMaxLiftDataSet.color = Color.RED
        exerciseMaxLiftDataSet.lineWidth = 3f
        chart.data = LineData(exerciseMaxLiftDataSet )
        chart.invalidate()
    }
}

class LabelFormatter: ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        val dateInst = Instant.fromEpochSeconds(value.toLong() * 60 * 60 * 24) // convert epoch day to epoch seconds
        val localDate = dateInst.toLocalDateTime(TimeZone.UTC)
        val dd = localDate.dayOfMonth
        val mm = localDate.month.value
        return "$dd/$mm"
    }
}








//---------------------------
class ChartAdapter() {
    lateinit var chart: Any
    lateinit var dateSet: Any



    constructor(_chart: BarChart ): this() {
        chart = _chart

    }

    constructor(_chart: LineChart): this() {
        chart = _chart
    }


}
