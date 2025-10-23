package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * HomeScreen compacto: sólo botones para navegar a Mi Ciclo y Cotizar.
 * Evita mostrar Blister aquí — el Blister debe estar en MiCicloScreen.
 */
@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MiBlisterApp", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("mi_ciclo") }) {
            Text("Mi ciclo")
        }

        Button(onClick = { navController.navigate("cotizar") }) {
            Text("Cotizar")
        }

        Spacer(Modifier.weight(1f))
        Text("Versión de desarrollo", style = MaterialTheme.typography.bodySmall)
    }
}