package com.example.blisterapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.blisterapp.ui.navigation.ServiceLocator
import com.example.blisterapp.ui.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa SessionManager seguro con applicationContext antes de setContent
        ServiceLocator.init(applicationContext)

        // DEBUG: imprime el estado inicial de la sesión para verificar persistencia
        val sm = ServiceLocator.sessionManager
        Log.d("SESSION_DEBUG", "after init: isLoggedIn=${sm?.isLoggedIn?.value}, token=${sm?.token}, userId=${sm?.currentUserId?.value}, biometric=${sm?.biometricEnabled}")

        setContent {
            // Aplica tu tema aquí si tienes uno
            NavGraph()
        }
    }
}