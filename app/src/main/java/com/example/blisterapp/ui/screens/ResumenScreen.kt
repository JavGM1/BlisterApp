package com.example.blisterapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blisterapp.viewmodel.UsuarioViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    Column(Modifier.padding(16.dp)) {
        Text("Resúmen del registro", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${estado.nombre}")
        Text("Correo: ${estado.correo}")
        Text("Dirección: ${estado.direccion}")
        Text("Contraseña: ${"*".repeat(estado.clave.length)}")
        Text("¿Términos Aceptados?: ${if (estado.aceptaTerminos) "Aceptados" else "Declinados"}")
    }
}