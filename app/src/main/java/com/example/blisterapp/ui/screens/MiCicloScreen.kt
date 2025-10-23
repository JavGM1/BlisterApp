package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blisterapp.ui.components.BlisterComposable
import com.example.blisterapp.ui.mi_ciclo.MiCicloViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla que consume MiCicloViewModel y muestra BlisterComposable.
 *
 * Integra un botón de ejemplo para marcar la pastilla de hoy (llama al ViewModel).
 * Ajusta la navegación a tu NavGraph donde corresponda.
 */
@Composable
fun MiCicloScreen(
    viewModel: MiCicloViewModel,
    onBack: () -> Unit = {}
) {
    val pills by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Mi Ciclo")
        BlisterComposable(
            pills = pills,
            onPillClicked = { index ->
                viewModel.toggleTaken(index)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Ejemplo: marcar la pastilla de hoy (si existe)
        Button(onClick = {
            coroutineScope.launch {
                val todayIndex = viewModel.getTodayIndex()
                if (todayIndex != null) viewModel.toggleTaken(todayIndex)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Marcar / Desmarcar pastilla de hoy")
        }
    }
}