package com.example.blisterapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun BlisterComposable(
    pillStates: List<String>, // use emoji or enums (🟢,🔴,🟡,🟣)
    onToggle: (index: Int) -> Unit = {}
) {
    // Draw 7 columns x 4 rows = 28
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val rows = pillStates.chunked(7)
        rows.forEachIndexed { rowIndex, row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEachIndexed { colIndex, state ->
                    val index = rowIndex * 7 + colIndex
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF2F2F2))
                            .border(1.dp, Color.LightGray, CircleShape)
                            .clickable { onToggle(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state)
                    }
                }
            }
        }
    }
}