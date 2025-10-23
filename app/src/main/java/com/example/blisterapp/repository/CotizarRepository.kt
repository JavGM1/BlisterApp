package com.example.blisterapp.repository

import com.example.blisterapp.ui.cotizar.PharmacyQuote
import com.example.blisterapp.network.ScraperService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * Repositorio de cotizaciones: realiza scraping de una lista de URLs y devuelve PharmacyQuote.
 * Implementación simple: realiza scraping en el hilo IO y no persiste (en memoria cache breve).
 */
interface CotizarRepository {
    suspend fun scrapeUrl(url: String): PharmacyQuote
}

/** Implementación simple basada en ScraperService */
class ScraperCotizarRepository(
    private val scraperService: ScraperService = ScraperService()
) : CotizarRepository {

    override suspend fun scrapeUrl(url: String): PharmacyQuote = withContext(Dispatchers.IO) {
        val result = scraperService.scrape(url)
        val name = result.title ?: url.substringAfter("//").substringBefore("/")
        val price = result.price
        PharmacyQuote(
            pharmacyId = url.substringAfter("//").substringBefore("/"),
            name = name,
            price = price,
            currency = result.currency,
            url = url,
            iconUrl = result.iconUrl,
            timestamp = Instant.now()
        )
    }
}