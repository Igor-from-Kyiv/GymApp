package com.example.gymapp

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

const val DEFAULT_DATABASE_ID = -1

@Serializable
class Workout() {
    var id = DEFAULT_DATABASE_ID // property for database relation ( initially -1, after recreating object from database id will have non negative integer )
    val exercisesDict: MutableMap <String, MutableList<Exercise>> = mutableMapOf()
    var date: Instant = Clock.System.now()
//    val maxLifts = mutableMapOf<String, Int>()
    val maxLifts: MutableMap <String, Int> = mutableMapOf()
    //var comment = ""
    //var duration = 0

    val workoutTotal: Int
        get(): Int {

            var total = 0
            this.exercisesDict.forEach { (k, v) ->
                total += v.sumOf { it.weight * it.reps }
            }
            return total
        }

    constructor (_date: Instant, ) : this() {
        date = _date
    }

    constructor ( _id: Int, _dateFromDB: Long, _exercises: MutableList<Exercise>) : this() {
        id = _id
        date = Instant.fromEpochMilliseconds(_dateFromDB)
        _exercises.forEach {exercise ->
            this.addExercise(exercise) }
    }

    fun getYear(): Int {
        return this.date.toLocalDateTime(TimeZone.UTC).year
    }

    fun getMonth(): Int {
        return this.date.toLocalDateTime(TimeZone.UTC).month.value
    }

    fun getDay(): Int {
        return this.date.toLocalDateTime(TimeZone.UTC).dayOfMonth
    }

    fun getEpochDay(): Long {
        return this.date.toEpochMilliseconds() / (60 * 60 * 24 * 1000)
    }

    fun addExercise(exercise: Exercise) {

        if (this.exercisesDict.contains(exercise.name)) {
            this.exercisesDict[exercise.name]!!.add(exercise)
        } else {
            this.exercisesDict.put(exercise.name, mutableListOf(exercise))
        }

        if (exercise.reps == 1) {
            this.maxLifts[exercise.name] = exercise.weight
        }
    }

    fun getMaxLift(exerciseName: String): Int {
        return maxLifts.getOrDefault(exerciseName, 0)
    }

}

@Serializable
class Exercise(val tag: Tag, val weight: Int, val reps: Int) {
    var id = DEFAULT_DATABASE_ID
    val name = tag.name

    constructor (_id: Int, _weight: Int, _reps: Int, tag: Tag): this(tag, _weight, _reps) {
        id = _id
    }
}

@Serializable
class Tag(val name: String,) {
    var id = DEFAULT_DATABASE_ID

    constructor(_id: Int, name: String): this(name) {
        id = _id
    }
}


