package com.example.blisterapp.ui.cotizar

import java.time.Instant

/**
 * Modelo simple para cotización de farmacia.
 */
data class PharmacyQuote(
    val pharmacyId: String,    // por ejemplo dominio o slug
    val name: String,
    val price: Double?,        // valor numérico en CLP (o null si no disponible)
    val currency: String? = "CLP",
    val url: String,
    val iconUrl: String? = null,
    val timestamp: Instant = Instant.now()
)