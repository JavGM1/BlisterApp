package com.example.blisterapp.auth

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256 // bits

    fun generateSalt(): ByteArray {
        val sr = SecureRandom()
        val salt = ByteArray(16)
        sr.nextBytes(salt)
        return salt
    }

    private fun getSecretKeyFactory(): SecretKeyFactory {
        // Intentamos SHA256 primero; si no está disponible, usar SHA1 como fallback.
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        } catch (t: Throwable) {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        }
    }

    fun hashPassword(password: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
        return try {
            val skf = getSecretKeyFactory()
            val key = skf.generateSecret(spec)
            key.encoded
        } finally {
            // limpiar datos sensibles en spec
            spec.clearPassword()
        }
    }

    fun toBase64(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)
    fun fromBase64(str: String): ByteArray = Base64.decode(str, Base64.NO_WRAP)

    fun verify(password: CharArray, saltBase64: String, hashBase64: String): Boolean {
        val salt = fromBase64(saltBase64)
        val computed = hashPassword(password, salt)
        val ok = toBase64(computed) == hashBase64
        // sobrescribir computed por seguridad
        computed.fill(0)
        return ok
    }
}