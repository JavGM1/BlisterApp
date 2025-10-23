package com.example.blisterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blisterapp.ui.screens.FormularioScreen
import com.example.blisterapp.ui.screens.HomeScreen
import com.example.blisterapp.ui.screens.ResumenScreen
import com.example.blisterapp.viewmodel.UsuarioViewModel

object Routes {
    const val REGISTER = "register"
    const val RESUMEN = "resumen"
    const val HOME = "home"
    const val MI_CICLO = "mi_ciclo"
    const val COTIZAR = "cotizar"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.REGISTER) {
        composable(Routes.REGISTER) {
            FormularioScreen(
                onRegistered = { navController.navigate(Routes.HOME) },
                viewModel = usuarioViewModel
            )
        }
        composable(Routes.RESUMEN) {
            ResumenScreen(viewModel = usuarioViewModel)
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
        // You can add nested composables for MI_CICLO and COTIZAR inside HomeScreen navigation
    }
}