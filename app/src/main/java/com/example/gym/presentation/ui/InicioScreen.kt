package com.example.gym.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.gym.presentation.Routes
import com.example.gym.presentation.Objective
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.wear.compose.material.Icon
import android.content.Context
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun InicioScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("gym_prefs", Context.MODE_PRIVATE)
    var isSyncEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("SYNC_ENABLED", false)) }

    Scaffold(
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Gym Trainer",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.title2,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
            }
            item {
                Button(
                    onClick = { navController.navigate(Routes.entrenamientoRoute(Objective.Masa.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = "Masa Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Masa", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
            item {
                Button(
                    onClick = { navController.navigate(Routes.entrenamientoRoute(Objective.Adelgazar.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsRun,
                            contentDescription = "Adelgazar Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Adelgazar", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
            item {
                Button(
                    onClick = { navController.navigate(Routes.entrenamientoRoute(Objective.Definir.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCut,
                            contentDescription = "Definir Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Definir", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
            item {
                Button(
                    onClick = { navController.navigate(Routes.entrenamientoRoute(Objective.Fuerza.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = "Fuerza Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Fuerza", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
            item {
                Button(
                    onClick = { navController.navigate(Routes.entrenamientoRoute(Objective.Resistencia.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsBike,
                            contentDescription = "Resistencia Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Resistencia", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        isSyncEnabled = !isSyncEnabled
                        sharedPreferences.edit().putBoolean("SYNC_ENABLED", isSyncEnabled).apply()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isSyncEnabled) Color.Green else MaterialTheme.colors.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Sync,
                            contentDescription = "Sync Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = MaterialTheme.colors.onSecondary
                        )
                        Text("Vincular dispositivo", color = MaterialTheme.colors.onSecondary)
                    }
                }
            }
        }
    }
}