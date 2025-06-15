package com.example.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import com.example.mobile2.data.DatabaseProvider
import com.example.mobile2.data.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.Alarm

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymTheme {
                val scope = rememberCoroutineScope()
                val dataClient: DataClient = Wearable.getDataClient(this)
                var workouts by remember { mutableStateOf(listOf<Workout>()) }

                // Cargar entrenamientos iniciales desde la base de datos
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        val db = DatabaseProvider.getDatabase(this@MainActivity)
                        workouts = db.workoutDao().getAllWorkouts()
                    }
                }

                // Escuchar datos del wearable
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
                                    withContext(Dispatchers.IO) {
                                        val db = DatabaseProvider.getDatabase(this@MainActivity)
                                        db.workoutDao().insert(workout)
                                        workouts = db.workoutDao().getAllWorkouts()
                                    }
                                }
                            }
                        }
                    }.await()
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Historial de Entrenamientos", color = MaterialTheme.colorScheme.onSurface) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(workouts) { workout ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.medium
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Encabezado: Objetivo + Fecha
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = workout.objective,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(workout.createdAt)),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }

                                    // Barra de progreso
                                    LinearProgressIndicator(
                                        progress = { workout.completionPercentage },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp),
                                        color = if (workout.completionPercentage >= 0.8f) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    )
                                    Text(
                                        text = "${"%.0f".format(workout.completionPercentage * 100)}% Completado",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )

                                    // Grid de estadísticas
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Columna 1: Calorías
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Whatshot,
                                                contentDescription = "Calorías",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "${workout.caloriesBurned} kcal",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        // Columna 2: Tiempo
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Alarm,
                                                contentDescription = "Tiempo",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "${workout.timeSpent / 60000}m",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
