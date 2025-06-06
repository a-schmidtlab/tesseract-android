package com.example.sofarotator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sofarotator.ui.screens.TesseractScreen
import com.example.sofarotator.ui.screens.InfoScreen

@Composable
fun SofaRotatorApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "tesseract"
    ) {
        composable("tesseract") {
            TesseractScreen(
                onNavigateToInfo = {
                    navController.navigate("info")
                }
            )
        }
        
        composable("info") {
            InfoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 