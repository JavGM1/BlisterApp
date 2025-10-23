package com.example.blisterapp.ui.cotizar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blisterapp.repository.CotizarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CotizarViewModel(
    private val repository: CotizarRepository,
    private val urlsToScrape: List<String>
) : ViewModel() {

    private val _quotes = MutableStateFlow<List<PharmacyQuote>>(emptyList())
    val quotes: StateFlow<List<PharmacyQuote>> = _quotes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadQuotes(force: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = mutableListOf<PharmacyQuote>()
            for (url in urlsToScrape) {
                try {
                    val q = repository.scrapeUrl(url)
                    results.add(q)
                } catch (e: Exception) {
                    results.add(PharmacyQuote(
                        pharmacyId = url.substringAfter("//").substringBefore("/"),
                        name = url,
                        price = null,
                        url = url,
                        timestamp = java.time.Instant.now()
                    ))
                }
            }
            // order by price asc with nulls last
            val ordered = results.sortedWith(compareByNullsLast())
            _quotes.value = ordered
            _isLoading.value = false
        }
    }

    private fun compareByNullsLast(): Comparator<PharmacyQuote> = Comparator { a, b ->
        when {
            a.price == null && b.price == null -> 0
            a.price == null -> 1
            b.price == null -> -1
            else -> a.price!!.compareTo(b.price!!)
        }
    }
}

/** Factory simple para crear ViewModel con repo y urls */
class CotizarViewModelFactory(
    private val repository: CotizarRepository,
    private val urls: List<String>
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CotizarViewModel::class.java)) {
            return CotizarViewModel(repository, urls) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}