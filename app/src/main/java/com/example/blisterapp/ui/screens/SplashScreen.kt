package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.blisterapp.auth.BiometricUtils
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.ui.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SplashScreen(navController: NavController, sessionManager: SessionManager) {
    val context = LocalContext.current
    var decided by remember { mutableStateOf(false) }

    // Pequeña UI de splash (puedes reemplazar con logo/branding)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    LaunchedEffect(Unit) {
        delay(300)

        sessionManager.isLoggedIn.collectLatest { _ ->
            if (decided) return@collectLatest

            val token = sessionManager.token
            val userId = sessionManager.currentUserId.value
            val hasValidSession = !token.isNullOrBlank() && userId.isNotBlank()

            fun goHome() {
                if (!decided) {
                    decided = true
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }

            fun goLogin() {
                if (!decided) {
                    decided = true
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }

            if (!hasValidSession) {
                goLogin()
                return@collectLatest
            }

            // Si hay sesión válida, si la biometría está habilitada la usamos; si no, vamos a Home
            if (sessionManager.biometricEnabled && BiometricUtils.isBiometricAvailable(context)) {
                BiometricUtils.authenticate(
                    context = context,
                    onSuccess = { goHome() },
                    onError = { /* si falla la biometric, forzamos login */ goLogin() }
                )
            } else {
                goHome()
            }
        }
    }
}