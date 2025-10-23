package com.example.blisterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blisterapp.ui.screens.HomeScreen
import com.example.blisterapp.ui.screens.LoginScreen
import com.example.blisterapp.ui.screens.SplashScreen
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.ui.screens.MiCicloScreen
import com.example.blisterapp.ui.screens.CotizarScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blisterapp.ui.navigation.ServiceLocator
import com.example.blisterapp.ui.mi_ciclo.MiCicloViewModel
import com.example.blisterapp.ui.navigation.MiCicloViewModelFactory
import com.example.blisterapp.ui.cotizar.CotizarViewModel
import com.example.blisterapp.ui.cotizar.CotizarViewModelFactory


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val sessionManager = ServiceLocator.sessionManager ?: return

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController, sessionManager = sessionManager)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, sessionManager = sessionManager)
        }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(Routes.MI_CICLO) {
            // Creamos el ViewModelFactory pasando sessionManager para que el ViewModel observe currentUserId
            val factory = MiCicloViewModelFactory(
                cycleRepository = ServiceLocator.cycleRepository,
                pillTakenRepository = ServiceLocator.pillTakenRepository,
                sessionManager = sessionManager
            )
            val miCicloViewModel: MiCicloViewModel = viewModel(factory = factory as ViewModelProvider.Factory)
            MiCicloScreen(viewModel = miCicloViewModel)
        }

        composable(Routes.COTIZAR) {
            val urls = listOf(
                "https://www.drsimi.cl/acotol-28-comprimidos/p",
                "https://www.cruzverde.cl/acotol-dienogest-2-mg-etinilestradiol-003-mg/265517.html",
                "https://recetasolidaria.cl/productos-adheridos/acotol-x-28-comprimidos-recubiertos/",
                "https://www.farmaciasahumada.cl/acotol-28-comprimidos-recubiertos-72253.html",
                "https://salcobrand.cl/products/acotol-b-28-comprimidos-recubiertos",
                "https://www.novasalud.cl/acotol-28-comprimidos-recubiertos"
            )

            val cotizarFactory = com.example.blisterapp.ui.cotizar.CotizarViewModelFactory(
                ServiceLocator.cotizarRepository,
                urls
            )

            val cotizarViewModel: com.example.blisterapp.ui.cotizar.CotizarViewModel =
                viewModel(factory = cotizarFactory as androidx.lifecycle.ViewModelProvider.Factory)

            CotizarScreen(viewModel = cotizarViewModel, onBack = { navController.popBackStack() })
        }

    }
}