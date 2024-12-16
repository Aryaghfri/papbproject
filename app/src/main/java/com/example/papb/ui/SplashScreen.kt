package com.example.papb.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 1000) // Animasi berlangsung selama 1 detik
    )

    LaunchedEffect(Unit) {
        delay(300) // Tampilkan tulisan dalam ukuran normal selama 0.5 detik
        scale = 2f // Perbesar tulisan hingga keluar layar
        delay(600) // Tunggu animasi zoom selesai
        onSplashComplete() // Masuk ke layar berikutnya
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Latar belakang tetap putih
        contentAlignment = Alignment.Center
    ) {
        // Tulisan Hazzbit dengan animasi zoom
        Text(
            text = "Hazzbit",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Color(0xFF006FFD) // Warna biru khas logo
            ),
            modifier = Modifier.scale(animatedScale)
        )
    }
}
