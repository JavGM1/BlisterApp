package com.example.blisterapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

class ScraperService(
    private val client: OkHttpClient = OkHttpClient()
) {

    private val pricePattern = Pattern.compile("\\$\\s*([\\d.,]+)")

    data class ScrapeResult(val price: Double?, val currency: String? = "CLP", val title: String?, val iconUrl: String?)

    suspend fun scrape(url: String): ScrapeResult {
        return try {
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "MiBlisterApp/1.0 (+https://example.com)")
                .build()
            val resp = client.newCall(req).execute()
            val body = resp.body?.string() ?: ""
            val doc = Jsoup.parse(body, url)
            val title = doc.selectFirst("meta[property=og:site_name]")?.attr("content")
                ?: doc.selectFirst("meta[property=og:title]")?.attr("content")
                ?: doc.title()

            // Mejor extracción de icon: usar absUrl para resolver URLs relativas
            var icon: String? = null
            val linkIcon = doc.selectFirst("link[rel~=(?i)^(shortcut icon|icon|apple-touch-icon)$]")
            if (linkIcon != null) {
                val href = linkIcon.attr("href")
                val abs = linkIcon.absUrl("href")
                icon = if (abs.isNullOrBlank()) {
                    // intentar concatenar base + href
                    if (href.startsWith("/")) {
                        val base = doc.baseUri().trimEnd('/')
                        "$base$href"
                    } else href
                } else abs
            } else {
                // fallback sencillo: intentar /favicon.ico
                val base = doc.baseUri()
                if (!base.isNullOrBlank()) {
                    icon = try {
                        val candidate = base.trimEnd('/') + "/favicon.ico"
                        candidate
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            // Try schema.org price (itemprop="price")
            val metaPrice = doc.selectFirst("[itemprop=price]")?.attr("content")
                ?: doc.selectFirst("meta[itemprop=price]")?.attr("content")
                ?: doc.selectFirst("meta[property=product:price:amount]")?.attr("content")

            val parsedPrice = metaPrice?.let { parsePriceString(it) } ?: run {
                val matcher = pricePattern.matcher(body)
                if (matcher.find()) {
                    parsePriceString(matcher.group(1))
                } else null
            }
            ScrapeResult(price = parsedPrice, currency = "CLP", title = title, iconUrl = icon)
        } catch (e: IOException) {
            ScrapeResult(price = null, currency = "CLP", title = null, iconUrl = null)
        }
    }

    private fun parsePriceString(raw: String): Double? {
        val cleaned = raw.replace(Regex("[^\\d]"), "")
        return if (cleaned.isBlank()) null else try {
            cleaned.toDouble()
        } catch (e: Exception) { null }
    }
}