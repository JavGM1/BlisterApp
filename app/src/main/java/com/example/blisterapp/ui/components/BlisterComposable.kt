package com.example.blisterapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.blisterapp.model.PillState
import com.example.blisterapp.ui.mi_ciclo.PillUiState

/**
 * BlisterComposable: recibe una lista de 28 PillUiState y dibuja una rejilla 7x4.
 *
 * onPillClicked(index) se llama cuando el usuario presiona una pastilla.
 *
 * Nota: personaliza tamaños/colors según tu tema.
 */
@Composable
fun BlisterComposable(
    pills: List<PillUiState>,
    onPillClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Ensure we have 28 items; if fewer, fill with placeholders
    val displayList = if (pills.size >= 28) pills.take(28) else {
        val padded = pills.toMutableList()
        while (padded.size < 28) {
            val idx = padded.size
            padded.add(PillUiState(idx, java.time.LocalDate.now().plusDays(idx.toLong()), PillState.Upcoming(java.time.LocalDate.now()), false))
        }
        padded.toList()
    }

    Column(modifier = modifier.padding(8.dp)) {
        for (row in 0 until 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val item = displayList[index]
                    PillItem(item = item, onClick = { onPillClicked(index) })
                }
            }
        }
    }
}

@Composable
private fun PillItem(item: PillUiState, onClick: () -> Unit) {
    val size = 44.dp
    val outerColor = when (item.state) {
        is PillState.Taken -> Color(0xFF2ECC71)    // green
        is PillState.Missed -> Color(0xFFE74C3C)   // red
        is PillState.Upcoming -> Color(0xFFF1C40F) // yellow
        is PillState.Placebo -> Color(0xFF8E44AD)  // purple
    }
    val border = if (item.isToday) BorderStroke(2.dp, Color(0xFF3B5998)) else null

    Surface(
        modifier = Modifier
            .size(size)
            .semantics { contentDescription = "Pill ${item.index + 1}: ${item.state.javaClass.simpleName}" }
            .clickable { onClick() },
        shape = CircleShape,
        color = Color.Transparent,
        border = border,
        shadowElevation = if (item.isToday) 6.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Outer circle (ring)
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F0F0))
            )
            // inner dot colored according to state
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(outerColor)
            )
        }
    }
}