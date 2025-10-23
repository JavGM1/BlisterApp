package com.example.blisterapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.blisterapp.ui.cotizar.CotizarViewModel
import com.example.blisterapp.ui.cotizar.PharmacyQuote
import com.example.blisterapp.R
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer

@Composable
fun CotizarScreen(
    viewModel: CotizarViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val quotes by viewModel.quotes.collectAsState()
    val loading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuotes()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Cotizar", modifier = Modifier.padding(bottom = 8.dp))
        Button(onClick = { viewModel.loadQuotes(force = true) }, modifier = Modifier.fillMaxWidth()) {
            Text("Actualizar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(quotes) { item ->
                CotizarItem(item = item) {
                    // open url
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    context.startActivity(intent)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun CotizarItem(item: PharmacyQuote, onClick: () -> Unit) {
    val context = LocalContext.current
    val priceText = item.price?.let {
        val fmt = NumberFormat.getNumberInstance(Locale("es","CL"))
        fmt.format(it)
    } ?: "No disponible"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Icono circular
            val iconUrl = item.iconUrl
            val imageModifier = Modifier
                .size(48.dp)
                .clip(CircleShape)

            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${item.name} icon",
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier,
                    error = painterResource(id = R.drawable.ic_pharmacy),
                    placeholder = painterResource(id = R.drawable.ic_pharmacy)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_pharmacy),
                    contentDescription = "fallback",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = priceText)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = item.url)
            }
        }
    }
}