package com.example.mobile2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val objective: String,
    val completionPercentage: Float,
    val caloriesBurned: Int,
    val timeSpent: Long,
    val createdAt: Long = System.currentTimeMillis()
)