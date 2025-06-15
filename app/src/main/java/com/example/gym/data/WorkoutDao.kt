package com.example.gym.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Proporciona métodos para insertar y consultar entrenamientos.
@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workouts ORDER BY id DESC")
    suspend fun getAllWorkouts(): List<Workout>

    @Query("DELETE FROM workouts WHERE createdAt < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)

}