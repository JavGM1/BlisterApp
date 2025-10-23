package com.example.blisterapp.auth

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Helpers seguros para autenticar con biometría.
 * - authenticate(...) acepta cualquier Context y busca una FragmentActivity dentro de él.
 * - Si no existe FragmentActivity o la biometría no está disponible, llama onError y no explota.
 */
object BiometricUtils {

    private fun findFragmentActivity(context: Context): FragmentActivity? {
        var ctx: Context = context
        while (ctx is ContextWrapper) {
            if (ctx is FragmentActivity) return ctx
            val base = ctx.baseContext ?: break
            if (base === ctx) break
            ctx = base
        }
        return null
    }

    fun isBiometricAvailable(context: Context): Boolean {
        val manager = BiometricManager.from(context)
        val res = manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        return res == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Autentica de forma segura. No lanza excepciones si no hay Activity adecuada.
     * - context: preferentemente una Activity-themed Context (ej. LocalContext.current)
     */
    fun authenticate(
        context: Context,
        title: String = "Autenticación",
        subtitle: String? = "Usa tu huella o biometría para entrar",
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val activity = findFragmentActivity(context)
        if (activity == null) {
            onError("No se encontró una Activity compatible para autenticación")
            return
        }

        val manager = BiometricManager.from(activity)
        val can = manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        if (can != BiometricManager.BIOMETRIC_SUCCESS) {
            onError("Biometría no disponible o no configurada en el dispositivo")
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .apply { if (!subtitle.isNullOrBlank()) setSubtitle(subtitle) }
            .setConfirmationRequired(false)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val prompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // no-op
            }
        })

        try {
            prompt.authenticate(promptInfo)
        } catch (t: Throwable) {
            onError("Error al invocar BiometricPrompt: ${t.message ?: "desconocido"}")
        }
    }
}