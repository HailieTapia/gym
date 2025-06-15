package com.example.mobile2.data

//Define la estructura de los datos almacenados
data class Workout(
    val id: Int = 0,
    val objective: String,
    val completionPercentage: Float,
    val caloriesBurned: Int,
    val timeSpent: Long,
    val createdAt: Long = System.currentTimeMillis()
)