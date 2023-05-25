# GymApp
#### Video Demo:  <URL HERE>
#### Description:
Android application for gym.
###### Idea of the app is to visualize progress of workouts and relation between total payload and maximum lift.

##### Purpose of the application:
- collect data (weight and repetitions for particular exercise) during a workout
- calculate workout total payload, exercise total payload and 1 repetition maximum
- display data with charts

##### View of the app
On the starter screen you see two charts. First shows two data sets. One is workouts total 
payload (total weight lifted during a workout) dataset,
and other shows data set of total payload of selected (or default) exercise.

##### Project structure
Among files that Android Studio create by default there are two types of files:
###### - Kotlin files (for behavior of the app)
Kotlin files which have 'Activity' in a name of the file like 'MainActivity.kt' responsible for handling
particular 'page' of the app. And the app has 3 'pages' in it:
-   starter page - MainActivity.kt
-   workout page - WorkoutHandlerActivity.kt
  - workouts history - WorkoutsLogActivity.kt

Models.kt - for classes of operable objects (workout, exercise ...)

AppDataBase.kt - handling app database (Sqlite)

CreateExerciseDialog.kt - handle dialog pop up window during creating new exercise for selection

Adapters.kt - contains adapter class AppCursorAdapter for handling data from database for select(spinner) element of UI

ChartUtils.kt - helps manage charts


###### - xml files (for drawing UI of the app)
-   starter page UI
- workout page UI
- workouts history UI






