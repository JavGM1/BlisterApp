package com.example.blisterapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.blisterapp.model.UsuarioErrores
import com.example.blisterapp.model.UsuarioUIState
import com.example.blisterapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application.applicationContext)

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "NO PUEDE ESTAR VACÍO" else null,
            correo = if (!estadoActual.correo.contains("@")) "CORREO INVÁLIDO" else null,
            clave = if (estadoActual.clave.length < 8) "DEBE TENER AL MENOS 8 CARACTERES" else null,
            direccion = if (estadoActual.direccion.isBlank()) "NO PUEDE ESTAR VACÍO" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    // Example of registering the user (simple hashing placeholder)
    fun register(onResult: (success: Boolean, userId: Long?) -> Unit) {
        viewModelScope.launch {
            val e = _estado.value
            if (!validarFormulario()) {
                onResult(false, null)
                return@launch
            }
            val hash = simpleHash(e.clave)
            val id = repository.registerUser(e.nombre, e.correo, hash)
            onResult(true, id)
        }
    }

    private fun simpleHash(input: String): String {
        // TODO: replace with secure hashing (e.g., bcrypt) or store via encrypted prefs
        return input.hashCode().toString()
    }
}