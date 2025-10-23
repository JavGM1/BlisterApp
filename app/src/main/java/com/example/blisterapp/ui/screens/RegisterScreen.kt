package com.example.blisterapp.ui.screens

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blisterapp.auth.SessionManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.blisterapp.ui.navigation.Routes
import com.example.blisterapp.repository.LocalAuthDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun RegisterScreen(
    navController: NavController,
    sessionManager: SessionManager,
    localAuthRepository: LocalAuthDataSource?
) {
    val repo = localAuthRepository ?: return

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var enableBiometric by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Repetir contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enableBiometric, onCheckedChange = { enableBiometric = it })
            Text("Usar huella para próximos accesos")
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                error = null
                if (username.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
                    error = "Completa todos los campos"
                    return@Button
                }
                if (password != confirm) {
                    error = "Las contraseñas no coinciden"
                    return@Button
                }

                scope.launch {
                    loading = true
                    try {
                        val result = withContext(Dispatchers.IO) {
                            repo.register(username.trim(), email.trim(), password.toCharArray())
                        }
                        val userId = result.first
                        val token = result.second

                        // persist session
                        sessionManager.login(tokenValue = token, enableBiometric = enableBiometric, userId = userId)
                        Log.d(
                            "SESSION_DEBUG",
                            "after register: isLoggedIn=${sessionManager.isLoggedIn.value}, token=${sessionManager.token}, userId=${sessionManager.currentUserId.value}, biometric=${sessionManager.biometricEnabled}"
                        )

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    } catch (t: Throwable) {
                        error = when (t) {
                            is SQLiteConstraintException -> "El usuario ya existe"
                            else -> t.localizedMessage ?: "Error al registrar"
                        }
                    } finally {
                        loading = false
                        // limpiar inputs por seguridad
                        password = ""
                        confirm = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(if (loading) "Registrando..." else "Crear cuenta")
        }

        TextButton(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("¿Ya tienes cuenta? Iniciar sesión")
        }
    }
}