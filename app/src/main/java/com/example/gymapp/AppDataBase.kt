package com.example.gymapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gymapp.AppDataBase.Companion.TAGS_NAME
import java.util.Calendar


class AppDataBase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        const val DATABASE_NAME = "GYM_DATABASE"
        const val DATABASE_VERSION = 1

        // workouts table
        const val WORKOUTS = "workouts"
        const val WORKOUTS_ID = "id"
        const val DATE = "date"

        // exercises table
        const val EXERCISES = "exercises"
        const val EXERCISES_ID = "id"
        const val WEIGHT = "weight"
        const val REPS = "reps"
        const val EXERCISE_TAG_ID = "tag_id"
        const val WORKOUT_ID = "workout_id"


        //tags table (exercise's names)
        const val TAGS = "tags"
        const val TAGS_ID = "_id"
        const val TAGS_NAME = "name"

        //default exercise tags (squat, press, dead lift)
        const val SQUAT = "squat"
        const val PRESS = "press"
        const val DEADLIFT = "dead lift"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // TODO: create db from backup file
        db.execSQL("CREATE TABLE $WORKOUTS (" +
                "$WORKOUTS_ID INTEGER PRIMARY KEY," +
                " $DATE INTEGER)"
        )
        db.execSQL("CREATE TABLE $EXERCISES (" +
                "$EXERCISES_ID INTEGER PRIMARY KEY," +
                " $DATE INTEGER," +
                " $WEIGHT INTEGER," +
                " $REPS INTEGER," +
                " $WORKOUT_ID INTEGER," +
                " $EXERCISE_TAG_ID INTEGER," +
                " FOREIGN KEY ($WORKOUT_ID) REFERENCES $WORKOUTS($WORKOUTS_ID)," +
                " FOREIGN KEY ($EXERCISE_TAG_ID) REFERENCES $TAGS($TAGS_ID))"
        )
        db.execSQL("CREATE TABLE $TAGS (" +
                "$TAGS_ID INTEGER PRIMARY KEY," +
                " $TAGS_NAME TEXT)"
        )
        db.execSQL("INSERT INTO $TAGS ($TAGS_NAME) VALUES ('squat')"
        )
        db.execSQL("INSERT INTO $TAGS ($TAGS_NAME) VALUES ('press')"
        )
        db.execSQL("INSERT INTO $TAGS ($TAGS_NAME) VALUES ('deadlift')"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO: create db file
        db.execSQL("DROP TABLE IF EXISTS $WORKOUTS")
        db.execSQL("DROP TABLE IF EXISTS $EXERCISES")
        db.execSQL("DROP TABLE IF EXISTS $TAGS")
        onCreate(db)
    }

    fun openReadableDBtoInspect() {
        this.readableDatabase.execSQL("Select * from workouts")
    }

    fun insertWorkout(workout: Workout): Long {
        // Get writable database
        val db = this.writableDatabase

        // Create ContentValues object with values to insert
        val values = ContentValues()
        values.put(DATE, workout.date.toEpochMilliseconds())

        // Insert new row into table and return id of new row
        val workoutId = db.insert(WORKOUTS, null, values)

        for (exercises in workout.exercisesDict.values) {
            exercises.forEach {exercise ->  insertExercise(exercise, workoutId)}
        }
        return workoutId
    }

    fun deleteWorkoutById(id: Int, range: Int? = null): Int {
        val db = this.readableDatabase
        val cursor: Cursor
        if (range == null) {
            cursor = db.rawQuery("DELETE FROM $WORKOUTS WHERE id = $id", null)
        } else {
           cursor = db.rawQuery("DELETE FROM $WORKOUTS WHERE id BETWEEN $id AND $range", null)
        }
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }

    fun insertExercise(exercise: Exercise, workoutID: Long) {
        // Get writable database
        val db = this.writableDatabase

        // Create ContentValues object with values to insert
        val vals = ContentValues()
        vals.put(WEIGHT, exercise.weight)
        vals.put(REPS, exercise.reps)
        vals.put(WORKOUT_ID, workoutID)
        val cursor = db.rawQuery("SELECT $TAGS_ID FROM $TAGS WHERE $TAGS_NAME = ?", arrayOf(exercise.name))
        cursor.moveToFirst()
        val tagId = cursor.getInt(cursor.getColumnIndexOrThrow(TAGS_ID))
        cursor.close()
        vals.put(EXERCISE_TAG_ID, tagId)
        db.insert(EXERCISES, null, vals)
        db.close()
    }

    fun fetchAllWorkoutsCursor(_db: SQLiteDatabase, dateInMilli: Long = 0): Cursor {
        val db = _db
        val cursor = db.rawQuery("SELECT $WORKOUTS_ID, $DATE FROM $WORKOUTS ORDER BY $DATE DESC", null)
        return cursor
    }

//    fun getWorkoutsList(startDate: Long, endDate: Long): MutableList<Workout> {
    fun getWorkoutsList(): MutableList<Workout> {

        val workoutsList: MutableList<Workout> = mutableListOf()
        val db = this.readableDatabase
        val cursor = this.fetchAllWorkoutsCursor(db)
        with(cursor) {
            while(cursor.moveToNext()) {
                val workoutId = getInt(getColumnIndexOrThrow("id"))
                val workoutDate = getLong(getColumnIndexOrThrow("date"))
                val exerciseList: MutableList<Exercise> = getExercisesByWorkoutId(workoutId)
                val workout = Workout(workoutId, workoutDate, exerciseList)
                workoutsList.add(workout)
            }
        }
        cursor.close()
        db.close()
        return workoutsList
    }

    fun getWorkoutsListByDate(startDate: Long, endDate: Long): MutableList<Workout> {
        val workoutsList: MutableList<Workout> = mutableListOf()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $WORKOUTS_ID, $DATE FROM $WORKOUTS WHERE $DATE > ? AND $DATE <= ?", arrayOf( startDate.toString(), endDate.toString()))
        with(cursor) {
            while(cursor.moveToNext()) {null
                val workoutId = getInt(getColumnIndexOrThrow("id"))
                val workoutDate = getLong(getColumnIndexOrThrow("date"))
                val exerciseList: MutableList<Exercise> = getExercisesByWorkoutId(workoutId)
                val workout = Workout(workoutId, workoutDate, exerciseList)
                workoutsList.add(workout)
            }
        }
        cursor.close()
        db.close()
        return workoutsList
    }

    fun getExercisesByWorkoutId(workoutId: Int): MutableList<Exercise> {
        val list = mutableListOf<Exercise>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $EXERCISES_ID, $WEIGHT, $REPS, $EXERCISE_TAG_ID FROM $EXERCISES WHERE $WORKOUT_ID = ?", arrayOf(workoutId.toString()))

        with(cursor) {

            while(moveToNext()) {
                val exerciseId = cursor.getInt(cursor.getColumnIndexOrThrow(EXERCISES_ID))
                val exerciseWeight = cursor.getInt(cursor.getColumnIndexOrThrow(WEIGHT))
                val exerciseReps = cursor.getInt(cursor.getColumnIndexOrThrow(REPS))
                val exerciseTagId = cursor.getInt(cursor.getColumnIndexOrThrow(EXERCISE_TAG_ID))

                val tag = getTagById(exerciseTagId)
                list.add(Exercise(exerciseId, exerciseWeight, exerciseReps, tag!!))
            }
        }
        cursor.close()
        db.close()
        return list
    }
    // TAG section
    fun dropTagsTable() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TAGS")
    }

    fun createTagsTable() {
        val db = this.readableDatabase
        db.execSQL("CREATE TABLE $TAGS (" +
                "$TAGS_ID INTEGER PRIMARY KEY," +
                " $TAGS_NAME TEXT)"
        )
    }

    fun insertTag(tagName: String): Long {
        if (getTagByNameOrNull(tagName) == null) {
            val db = this.writableDatabase

            // Create ContentValues object with values to insert
            val vals = ContentValues()
            vals.put(TAGS_NAME, tagName)
            return db.insert(TAGS, null, vals)
        }
        return -1L
    }

    fun getTagById(tagId: Int): Tag? {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $TAGS_NAME FROM $TAGS WHERE $TAGS_ID = $tagId", null)
        if (cursor.moveToFirst()) {
            val tagName = cursor.getString(cursor.getColumnIndexOrThrow(TAGS_NAME))
            cursor.close()
            db.close()
            return Tag(tagId, tagName)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getTagByNameOrNull(name: String): Tag? {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $TAGS_ID, $TAGS_NAME FROM $TAGS WHERE $TAGS_NAME = ?", arrayOf(name))
        if (cursor.moveToFirst()) {
            val tagName = cursor.getString(cursor.getColumnIndexOrThrow(TAGS_NAME))
            val tagId = cursor.getInt(cursor.getColumnIndexOrThrow(TAGS_ID))
            cursor.close()
            db.close()
            return Tag(tagId, tagName)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getTagID(tag: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $TAGS_ID FROM $TAGS WHERE $TAGS_NAME = $tag", null)
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TAGS_ID))
            cursor.close()
            db.close()
            return id
        } else {
            cursor.close()
            db.close()
            null
        }
    }
    fun fetchTags(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT $TAGS_ID, $TAGS_NAME FROM $TAGS", null)
    }

    fun getAllTags(): List<Tag> {
        val db = this.readableDatabase
        val tagsList = mutableListOf<Tag>()
        val cursor = db.rawQuery("SELECT $TAGS_ID, $TAGS_NAME FROM $TAGS", null)

        with(cursor) {
            while(moveToNext()) {
                val tagId = cursor.getInt(cursor.getColumnIndexOrThrow(TAGS_ID))
                val tagName = cursor.getString(cursor.getColumnIndexOrThrow(TAGS_NAME))
                tagsList.add(Tag(tagId, tagName))
            }
        }
//        cursor.close()
//        db.close()
        return tagsList
    }

    fun updateTagsName(name: String) {
        val db = this.writableDatabase
        db.rawQuery("UPDATE tags SET name = $name WHERE name = null", null)
    }
}