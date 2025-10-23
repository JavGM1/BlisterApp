package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blisterapp.ui.components.BlisterComposable

@Composable
fun HomeScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MiBlisterApp", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
        Button(onClick = { /* navigate to Mi Ciclo */ }) {
            Text("Mi ciclo")
        }
        Button(onClick = { /* navigate to Cotizar */ }) {
            Text("Cotizar")
        }

        Spacer(Modifier.height(24.dp))
        // muestra ejemplo del blíster
        BlisterComposable(
            pillStates = List(28) { index ->
                when {
                    index < 21 -> "🟡" // placeholder: active future
                    else -> "🟣" // placebo
                }
            }
        )
    }
}