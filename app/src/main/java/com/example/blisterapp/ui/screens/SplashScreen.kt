package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.ui.navigation.Routes
import com.example.blisterapp.ui.navigation.ServiceLocator
import com.example.blisterapp.repository.LocalAuthRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(navController: NavController, sessionManager: SessionManager) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val repoImpl = ServiceLocator.localAuthRepository as? LocalAuthRepositoryImpl

        val hasUsers = withContext(Dispatchers.IO) {
            try {
                repoImpl?.hasUsers() ?: false
            } catch (t: Throwable) {
                false
            }
        }

        val target = if (hasUsers) Routes.LOGIN else Routes.REGISTER
        navController.navigate(target) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("BlisterApp")
    }
}