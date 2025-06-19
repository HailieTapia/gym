package com.example.mobile2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workouts ORDER BY createdAt DESC")
    suspend fun getAllWorkouts(): List<Workout>
}