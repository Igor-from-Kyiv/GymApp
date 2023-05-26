# GymApp
#### Video Demo:  <URL HERE>
#### Description:
Android application for gym.
Idea of the app is to visualize progress of workouts and relation between total payload and maximum lift.

#### Purpose of the application:
- collect data (weight and repetitions for particular exercise) during a workout
- calculate workout total payload, exercise total payload and 1 repetition maximum
- display data with charts

#### View of the app
On the starter screen you see two charts. First shows two data sets. One is workouts total 
payload (total weight lifted during a workout) dataset,
and other shows data set of total payload of selected (or default) exercise.

#### Project structure
Among files that Android Studio create by default there are two types of files: 
-     Kotlin files (for behavior of the app) 
-     xml files (for drawing UI of the app)

#### Kotlin files
- Kotlin files which have 'Activity' in a name of the file like 'MainActivity.kt' responsible for handling
particular 'page' of the app. And the app has 3 'pages' in it:
    - starter page - MainActivity.kt (Retrieves starter page view object. Initialize and set up charts. 
        Manage date picker and selection of exercise to display in charts)
    -   workout page - WorkoutHandlerActivity.kt (Retrieves view object of workout page. Manage creating of new exercise type.
          Collect user data through text edit elements. Creates Workout, Exercise and Tag objects
          and initiate saving them into database)
    - workouts history - WorkoutsLogActivity.kt (Retrieves workouts list view object. Retrieves workouts dataset from database.
        Retrieves recycler view object (scrollable list) and set its adapter with ItemAdapter instance)

- Models.kt - contains models (classes) of operable objects (workout, exercise ...)

- AppDataBase.kt - handling app database (Sqlite). Initialize database, CRUD operations.

- CreateExerciseDialog.kt - handle dialog pop up window during creating new exercise for selecting exercise name.

- Adapters.kt - contains adapter class AppCursorAdapter for handling data from database for select(spinner) element of UI

- ChartUtils.kt - helps manage charts.

#### XML files
- activity_main.xml - starter page UI:
  - Date picker
  - exercise selector
  - workout total payload chart
  - workout max lift chart 
  - 'workouts log' button
  - 'start workout' button
- activity_workout_handler.xml - workout page UI
  - create exercise button
  - select exercise
  - weight input
  - reps input
  - 'add' exercise to workout button
  - 'stop' workout button
- create_exercise_dialog.xml - dialog pop up layout. Contains text input for exercise name
- activity_workouts_list.xml - workouts history UI. FrameLayout that contains RecyclerView element
- workout_list_item.xml - layout for RecyclerView item. will be used to render each element of scrollable list






