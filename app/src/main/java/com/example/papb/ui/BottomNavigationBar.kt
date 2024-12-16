package com.example.papb.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("list", "recap", "profile")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.DateRange,
        Icons.Filled.AccountCircle
    )

    NavigationBar(
        containerColor = Color(0xFFF4F8FF) // Warna latar belakang NavigationBar
    ) {
        items.forEachIndexed { index, item ->
            // Animasi skala ketika dipilih
            val scale = remember { Animatable(1f) }
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(selectedItem) {
                if (selectedItem == index) {
                    coroutineScope.launch {
                        scale.animateTo(
                            targetValue = 1.2f, // Membesar 20%
                            animationSpec = tween(durationMillis = 300)
                        )
                        scale.animateTo(
                            targetValue = 1f, // Kembali ke ukuran normal
                            animationSpec = tween(durationMillis = 200)
                        )
                    }
                }
            }

            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        modifier = Modifier.scale(scale.value),
                        tint = animateColorAsState(
                            targetValue = if (selectedItem == index) Color(0xFF006FFD) else Color(0xFFC5C9CC),
                            animationSpec = tween(durationMillis = 300)
                        ).value
                    )
                },
            )
        }
    }
}

