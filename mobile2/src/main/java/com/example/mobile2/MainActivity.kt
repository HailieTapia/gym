package com.example.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile2.ui.theme.GymTheme
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.items


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymTheme {
                val scope = rememberCoroutineScope()
                val dataClient: DataClient = Wearable.getDataClient(this)
                var workouts by remember { mutableStateOf(listOf<Workout>()) }

                LaunchedEffect(Unit) {
                    dataClient.addListener { dataEvents: DataEventBuffer ->
                        dataEvents.forEach { event ->
                            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/workout_data") {
                                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                                val workout = Workout(
                                    id = 0,
                                    objective = dataMap.getString("objective") ?: "Unknown",
                                    completionPercentage = dataMap.getFloat("completionPercentage", 0f),
                                    caloriesBurned = dataMap.getInt("caloriesBurned", 0),
                                    timeSpent = dataMap.getLong("timeSpent", 0L),
                                    createdAt = dataMap.getLong("createdAt", 0L)
                                )
                                scope.launch {
                                    workouts = workouts + workout
                                }
                            }
                        }
                    }.await()
                }

                Scaffold(
                    topBar = { TopAppBar(title = { Text("Historial de Entrenamientos") }) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        items(workouts) { workout ->
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    text = "Objetivo: ${workout.objective}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Completado: ${"%.0f".format(workout.completionPercentage * 100)}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Calorías: ${workout.caloriesBurned}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Tiempo: ${workout.timeSpent / 60000}m",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(workout.createdAt))}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Workout(
    val id: Int = 0,
    val objective: String,
    val completionPercentage: Float,
    val caloriesBurned: Int,
    val timeSpent: Long,
    val createdAt: Long
)