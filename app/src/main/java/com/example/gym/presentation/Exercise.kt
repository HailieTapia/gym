package com.example.gym.presentation

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val met: Double
)

fun getExercisesForObjective(objective: Objective): List<Exercise> {
    return when (objective) {
        Objective.Masa -> listOf(
            Exercise("Push-ups", 3, 12, met = 8.0),
            Exercise("Squats", 3, 15, met = 5.0),
            Exercise("Bench Press", 3, 10, met = 6.0)
        )
        Objective.Adelgazar -> listOf(
            Exercise("Jumping Jacks", 3, 30, met = 8.0),
            Exercise("Running in Place", 3, 60, met = 8.0),
            Exercise("Burpees", 3, 15, met = 10.0)
        )
        Objective.Definir -> listOf(
            Exercise("Dumbbell Curls", 4, 12, met = 3.5),
            Exercise("Lunges", 4, 12, met = 5.0),
            Exercise("Plank", 3, 30, met = 3.0)
        )
        Objective.Fuerza -> listOf(
            Exercise("Deadlifts", 3, 8, met = 6.0),
            Exercise("Pull-ups", 3, 10, met = 8.0),
            Exercise("Overhead Press", 3, 8, met = 5.5)
        )
        Objective.Resistencia -> listOf(
            Exercise("High Knees", 3, 40, met = 7.5),
            Exercise("Mountain Climbers", 3, 20, met = 8.0),
            Exercise("Jump Rope", 3, 50, met = 10.0)
        )
    }
}
