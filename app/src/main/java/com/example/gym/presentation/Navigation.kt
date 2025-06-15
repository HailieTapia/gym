package com.example.gym.presentation

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import com.example.gym.presentation.ui.InicioScreen
import com.example.gym.presentation.ui.EntrenamientoScreen
import com.example.gym.presentation.ui.ResumenScreen
import androidx.navigation.navArgument

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.INICIO) {
        composable(Routes.INICIO) {
            InicioScreen(navController = navController)
        }
        composable(
            route = Routes.ENTRENAMIENTO,
            arguments = listOf(navArgument("objective") { type = NavType.StringType })
        ) {
            EntrenamientoScreen(
                navController = navController,
                objectiveString = it.arguments?.getString("objective") ?: ""
            )
        }
        composable(
            route = "${Routes.RESUMEN}?completion={completion}&calories={calories}&time={time}&objective={objective}",
            arguments = listOf(
                navArgument("completion") { type = NavType.FloatType; defaultValue = 0f },
                navArgument("calories") { type = NavType.IntType; defaultValue = 0 },
                navArgument("time") { type = NavType.LongType; defaultValue = 0L },
                navArgument("objective") { type = NavType.StringType; defaultValue = "Unknown" }
            )
        ) {
            ResumenScreen(navController = navController)
        }
    }
}

object Routes {
    const val INICIO = "inicio"
    const val ENTRENAMIENTO = "entrenamiento/{objective}"
    const val RESUMEN = "resumen"
    fun entrenamientoRoute(objective: String) = "entrenamiento/$objective"
}