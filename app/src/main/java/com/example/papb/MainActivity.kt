package com.example.papb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.papb.MainApp
import com.example.papb.ui.theme.PapbTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PapbTheme {
                MainApp()
            }
        }
    }
}
