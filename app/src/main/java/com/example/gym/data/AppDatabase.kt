package com.example.gym.data

import androidx.room.Database
import androidx.room.RoomDatabase

//Configura la base de datos con Room y usa DatabaseProvider para instanciarla de forma segura como singleton.
//Los datos se guardan automáticamente al finalizar un entrenamiento en ResumenScreen.

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}

