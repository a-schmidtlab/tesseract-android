package com.example.sofarotator

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sofarotator.ui.theme.SofaRotatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Lock orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        enableEdgeToEdge()
        setContent {
            SofaRotatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SofaRotatorApp()
                }
            }
        }
    }
} 