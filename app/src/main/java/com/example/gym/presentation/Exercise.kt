package com.example.gym.presentation

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int
)

fun getExercisesForObjective(objective: Objective): List<Exercise> {
    return when (objective) {
        Objective.Masa -> listOf(
            Exercise("Push-ups", 3, 12),
            Exercise("Squats", 3, 15),
            Exercise("Bench Press", 3, 10)
        )
        Objective.Adelgazar -> listOf(
            Exercise("Jumping Jacks", 3, 30),
            Exercise("Running in Place", 3, 60),
            Exercise("Burpees", 3, 15)
        )
        Objective.Definir -> listOf(
            Exercise("Dumbbell Curls", 4, 12),
            Exercise("Lunges", 4, 12),
            Exercise("Plank", 3, 30)
        )
        Objective.Fuerza -> listOf(
            Exercise("Deadlifts", 3, 8),
            Exercise("Pull-ups", 3, 10),
            Exercise("Overhead Press", 3, 8)
        )
        Objective.Resistencia -> listOf(
            Exercise("High Knees", 3, 40),
            Exercise("Mountain Climbers   ers", 3, 20),
            Exercise("Jump Rope", 3, 50)
        )
    }
}