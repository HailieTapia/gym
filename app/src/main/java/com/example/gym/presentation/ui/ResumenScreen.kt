package com.example.gym.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.gym.data.Workout
import com.example.gym.di.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.ButtonDefaults
import com.example.gym.presentation.Routes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Icon
import android.content.Context
import androidx.compose.runtime.*
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ResumenScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val args = navBackStackEntry?.arguments

    val completionPercentage = args?.getFloat("completion") ?: 0f
    val caloriesBurned = args?.getInt("calories") ?: 0
    val timeSpent = args?.getLong("time") ?: 0L
    val objective = args?.getString("objective") ?: "Unknown"

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataClient: DataClient = Wearable.getDataClient(context)
    val sharedPreferences = context.getSharedPreferences("gym_prefs", Context.MODE_PRIVATE)
    val isSyncEnabled = sharedPreferences.getBoolean("SYNC_ENABLED", false)

    // Estado para el workout
    val workout = remember {
        mutableStateOf(
            Workout(
                objective = objective,
                completionPercentage = completionPercentage,
                caloriesBurned = caloriesBurned,
                timeSpent = timeSpent,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    // Actualizar workout cuando cambien los args
    LaunchedEffect(completionPercentage, caloriesBurned, timeSpent, objective) {
        workout.value = Workout(
            objective = objective,
            completionPercentage = completionPercentage,
            caloriesBurned = caloriesBurned,
            timeSpent = timeSpent,
            createdAt = System.currentTimeMillis()
        )
    }

    Scaffold(
        timeText = { TimeText() },
        modifier = Modifier.background(MaterialTheme.colors.surface)
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Éxito",
                    tint = Color(0xFF06DA01),
                    modifier = Modifier.size(24.dp)
                )
            }
            item {
                Text(
                    text = "¡Terminado!",
                    style = MaterialTheme.typography.caption1,
                    color = Color(0xFF06DA01),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = "${(completionPercentage * 100).toInt()}%",
                    style = MaterialTheme.typography.display1,
                    color = Color(0xFF06DA01),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "$caloriesBurned",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "${timeSpent / 60000}m",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "${(completionPercentage * 3).toInt()}/3",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "kcal",
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Light),
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = "tiempo",
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Light),
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = "ejercicios",
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Light),
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Button(
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                val db = DatabaseProvider.getDatabase(context)
                                Log.d("ResumenScreen", "Inserting workout locally: ${workout.value}")
                                db.workoutDao().insert(workout.value)
                                if (isSyncEnabled) {
                                    val dataMapRequest = PutDataMapRequest.create("/workout_data")
                                    with(dataMapRequest.dataMap) {
                                        putString("objective", workout.value.objective)
                                        putFloat("completionPercentage", workout.value.completionPercentage)
                                        putInt("caloriesBurned", workout.value.caloriesBurned)
                                        putLong("timeSpent", workout.value.timeSpent)
                                        putLong("createdAt", workout.value.createdAt)
                                    }
                                    Log.d("ResumenScreen", "Sending workout to mobile: ${workout.value}")
                                    try {
                                        dataClient.putDataItem(dataMapRequest.asPutDataRequest()).await()
                                        Log.d("ResumenScreen", "Workout sent successfully")
                                    } catch (e: Exception) {
                                        Log.e("ResumenScreen", "Sync failed", e)
                                    }
                                }
                            }
                            navController.navigate(Routes.INICIO) {
                                popUpTo(0)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Volver al Inicio", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}



