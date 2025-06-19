package com.example.gym.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.gym.presentation.Objective
import com.example.gym.presentation.Routes
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.gym.presentation.getExercisesForObjective
import kotlin.collections.count
import kotlin.collections.sumOf
import kotlin.collections.toMutableList
import kotlin.ranges.coerceAtMost

@Composable
fun EntrenamientoScreen(navController: NavController, objectiveString: String) {
    val objective = Objective.valueOf(objectiveString)
    val exercises = getExercisesForObjective(objective)
    val totalExercises = exercises.size
    val completedExercises = remember { mutableStateOf(0) }
    val progress = if (totalExercises > 0) completedExercises.value.toFloat() / totalExercises else 0f
    val exerciseCompletedStates = remember { List(exercises.size) { mutableStateOf(false) }.toMutableList() }
    val seriesCounters = remember { List(exercises.size) { mutableStateOf(0) } }
    val setTimestamps = remember { List(exercises.size) { mutableStateListOf<Long>() } }
    val timeSpent = remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            timeSpent.value += 1000L
        }
    }
    fun calcularCalorias(met: Double, minutos: Double, peso: Double = 65.0): Int {
        return ((met * 3.5 * peso / 200) * minutos).toInt()
    }
    val caloriesBurned = exercises.mapIndexed { index, ejercicio ->
        val timestamps = setTimestamps[index]
        val duracionesMs = timestamps.zipWithNext { a, b -> b - a }  // calcula las diferencias
        val totalDuracionMs = duracionesMs.sum()
        val totalMin = totalDuracionMs / 60000.0
        calcularCalorias(ejercicio.met, totalMin)
    }.sum()


    Scaffold(
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item {
                Text(
                    text = "Rutina para ${objective.name}",
                    style = MaterialTheme.typography.title3.copy(fontSize = 16.sp),
                    color = MaterialTheme.colors.primary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }
            items(exercises.size) { index ->
                val exercise = exercises[index]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.name,
                                fontSize = 10.sp,
                                color = Color(0xFFE0E0E0)
                            )
                            Text(
                                text = "${exercise.sets} × ${exercise.reps}",
                                fontSize = 8.sp,
                                color = Color(0xFFFC5A03)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val currentSeries = seriesCounters[index].value
                            val totalSeries = exercise.sets
                            val isFullyCompleted = currentSeries >= totalSeries

                            // Botón -
                            Button(
                                onClick = {
                                    if (currentSeries > 0) {
                                        seriesCounters[index].value = currentSeries - 1
                                        if (currentSeries - 1 < totalSeries) {
                                            exerciseCompletedStates[index].value = false
                                            completedExercises.value = exerciseCompletedStates.count { it.value }
                                        }
                                    }
                                },
                                enabled = currentSeries > 0,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent.copy(alpha = 0.7f),
                                    contentColor = Color.Red
                                ),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 2.dp)
                            ) {
                                Text("-", fontSize = 12.sp)
                            }

                            // Contador
                            Text(
                                text = "$currentSeries/$totalSeries",
                                fontSize = 8.sp,
                                color = Color(0xFF888888)
                            )

                            // Botón +
                            Button(
                                onClick = {
                                    val newValue = (currentSeries + 1).coerceAtMost(totalSeries)
                                    seriesCounters[index].value = newValue
                                    setTimestamps[index].add(System.currentTimeMillis())

                                    if (newValue == totalSeries) {
                                        exerciseCompletedStates[index].value = true
                                        completedExercises.value = exerciseCompletedStates.count { it.value }
                                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                                    }
                                },
                                enabled = !isFullyCompleted,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent.copy(alpha = 0.7f),
                                    contentColor = Color.Green
                                ),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(start = 2.dp)
                            ) {
                                Text("+", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        navController.navigate(
                            "${Routes.RESUMEN}?completion=${progress}&calories=$caloriesBurned&time=${timeSpent.value}&objective=${objective.name}"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(
                        text = "Finalizar rutina",
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}