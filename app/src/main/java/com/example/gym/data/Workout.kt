package com.example.gym.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Define la estructura de los datos almacenados
@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val objective: String,
    val completionPercentage: Float,
    val caloriesBurned: Int,
    val timeSpent: Long, // Tiempo invertido en milisegundos
    val createdAt: Long = System.currentTimeMillis() // Timestamp de creación
)