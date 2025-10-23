package com.example.blisterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blisterapp.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado.asStateFlow()

    fun actualizarNombre(nuevo: String) {
        _estado.value = _estado.value.copy(nombre = nuevo)
    }

    fun actualizarCorreo(nuevo: String) {
        _estado.value = _estado.value.copy(correo = nuevo)
    }

    fun actualizarClave(nueva: String) {
        _estado.value = _estado.value.copy(clave = nueva)
    }

    fun actualizarDireccion(nueva: String) {
        _estado.value = _estado.value.copy(direccion = nueva)
    }

    fun actualizarAceptaTerminos(valor: Boolean) {
        _estado.value = _estado.value.copy(aceptaTerminos = valor)
    }

    fun validarYGuardar() {
        viewModelScope.launch {
            val s = _estado.value
            var nombreErr: String? = null
            var correoErr: String? = null
            var claveErr: String? = null

            if (s.nombre.isBlank()) nombreErr = "Nombre requerido"
            if (s.correo.isBlank()) correoErr = "Correo requerido"
            if (s.clave.length < 6) claveErr = "Clave debe tener al menos 6 caracteres"

            val errores = s.errores.copy(
                nombre = nombreErr,
                correo = correoErr,
                clave = claveErr
            )

            _estado.value = s.copy(errores = errores)
        }
    }
}