package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blisterapp.ui.mi_ciclo.CycleEntity
import com.example.blisterapp.ui.navigation.Routes
import com.example.blisterapp.ui.navigation.ServiceLocator
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FormularioScreen(navController: NavController) {
    val cycleRepository = ServiceLocator.cycleRepository
    val sessionManager = ServiceLocator.sessionManager ?: return

    val userId by sessionManager.currentUserId.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Completa tu información", style = MaterialTheme.typography.headlineSmall)

        Text("Se guardará la fecha de inicio del ciclo como hoy. Puedes cambiar esto más tarde.")

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(onClick = {
            error = null
            if (userId.isBlank()) {
                error = "Usuario no identificado"
                return@Button
            }
            loading = true
            scope.launch {
                try {
                    val startIsoDate = LocalDate.now()
                    val cycle = CycleEntity(userId = userId, startDate = startIsoDate)
                    cycleRepository.setCycleForUser(userId, cycle)
                    // Navegar a Home
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.FORMULARIO) { inclusive = true }
                    }
                } catch (t: Throwable) {
                    error = t.localizedMessage ?: "Error al guardar"
                } finally {
                    loading = false
                }
            }
        }, enabled = !loading, modifier = Modifier.fillMaxWidth()) {
            Text(if (loading) "Guardando..." else "Guardar y continuar")
        }
    }
}