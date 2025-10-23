package com.example.blisterapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blisterapp.auth.SessionManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Alignment
import com.example.blisterapp.ui.navigation.Routes

/**
 * LoginScreen simple: en modo desarrollo acepta cualquier credencial y guarda token ficticio.
 * Reemplazar la lógica de login por llamado a backend en producción.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    sessionManager: SessionManager
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var enableBiometric by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Bienvenida a MiBlisterApp", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enableBiometric, onCheckedChange = { enableBiometric = it })
            Text("Usar huella para próximos accesos")
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                error = "Ingresa usuario y contraseña"
                return@Button
            }
            // MOCK: genera token ficticio. En producción, llamar backend y obtener token real.
            val fakeToken = "token_for_${username.trim()}"
            // Guardamos token y también el userId (aquí usamos el username como userId).
            sessionManager.login(tokenValue = fakeToken, enableBiometric = enableBiometric, userId = username.trim())

            // DEBUG: verificar que los valores quedaron guardados
            Log.d("SESSION_DEBUG", "after login: isLoggedIn=${sessionManager.isLoggedIn.value}, token=${sessionManager.token}, userId=${sessionManager.currentUserId.value}, biometric=${sessionManager.biometricEnabled}")

            // Navegar al Home y limpiar backstack
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Iniciar sesión")
        }
    }
}