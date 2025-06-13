package com.danielhermoso.cliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danielhermoso.cliente.screen.AppMain
import com.danielhermoso.cliente.ui.theme.ClienteTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClienteTheme {
                AppMain()
            }
        }
    }
}
