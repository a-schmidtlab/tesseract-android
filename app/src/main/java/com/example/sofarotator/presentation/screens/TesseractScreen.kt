package com.example.sofarotator.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sofarotator.presentation.state.TesseractViewModel
import com.example.sofarotator.presentation.components.TesseractCanvas
import com.example.sofarotator.utils.motion.rememberFrameCallback
import com.example.sofarotator.utils.motion.rememberMotionManager
import com.example.sofarotator.utils.motion.MotionData

/**
 * Main tesseract visualization screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TesseractScreen(
    onNavigateToInfo: () -> Unit,
    viewModel: TesseractViewModel = viewModel()
) {
    val state by viewModel.state
    
    // Motion sensor integration
    val motionManager = rememberMotionManager()
    
    // Collect motion data for device-based rotation
    MotionData(
        motionManager = motionManager,
        onGyroscopeChange = { (x, y, z) ->
            if (state.motionEnabled) {
                // Map gyroscope data to tesseract rotation
                viewModel.applyMotionInput(x, y, z)
            }
        }
    )
    
    // Frame callback for animation - always enabled for smooth rotation
    rememberFrameCallback(
        enabled = true
    ) { deltaTime ->
        viewModel.updateRotations(deltaTime)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Main tesseract visualization
        TesseractCanvas(
            state = state,
            modifier = Modifier.fillMaxSize()
        )
        
        // Top app bar
        TopAppBar(
            title = { 
                Text(
                    text = "SofaRotator",
                    color = Color.White
                ) 
            },
            navigationIcon = {
                IconButton(onClick = { viewModel.toggleControls() }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.Cyan
                    )
                }
            },
            actions = {
                // Play/Pause button
                IconButton(onClick = { viewModel.toggleAutoRotation() }) {
                    Icon(
                        if (state.isAutoRotating) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (state.isAutoRotating) "Pause" else "Play",
                        tint = if (state.isAutoRotating) Color.Green else Color.Gray
                    )
                }
                
                // Info button
                IconButton(onClick = onNavigateToInfo) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Information",
                        tint = Color.Cyan
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            ),
            modifier = Modifier.align(Alignment.TopCenter)
        )
        
        // Status indicator at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visualization mode indicator
            Text(
                text = state.visualizationMode.name.lowercase().replaceFirstChar { it.uppercase() },
                color = Color.Cyan,
                style = MaterialTheme.typography.bodySmall
            )
            
            // Rotation status
            if (state.isAutoRotating) {
                Text(
                    text = "Auto Rotating",
                    color = Color.Green,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "Manual Control",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Cross-section indicator
            if (state.showCrossSection) {
                Text(
                    text = "Cross-Section",
                    color = Color(0xFF9C27B0), // Purple
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        
        // Advanced Controls Panel
        if (state.showControls) {
            com.example.sofarotator.ui.components.RotationControlsPanel(
                state = state,
                onVisualizationModeChange = { viewModel.setVisualizationMode(it) },
                onProjectionModeChange = { viewModel.setProjectionMode(it) },
                onAutoRotationToggle = { viewModel.toggleAutoRotation() },
                onAutoRotationSpeedChange = { viewModel.setAutoRotationSpeed(it) },
                onXWSpeedChange = { viewModel.setXWSpeed(it) },
                onYZSpeedChange = { viewModel.setYZSpeed(it) },
                onXYSpeedChange = { viewModel.setXYSpeed(it) },
                onZWSpeedChange = { viewModel.setZWSpeed(it) },
                onXWSliderChange = { viewModel.setXWSliderPosition(it) },
                onYZSliderChange = { viewModel.setYZSliderPosition(it) },
                onXYSliderChange = { viewModel.setXYSliderPosition(it) },
                onZWSliderChange = { viewModel.setZWSliderPosition(it) },
                onXWLockToggle = { viewModel.toggleXWLock() },
                onYZLockToggle = { viewModel.toggleYZLock() },
                onXYLockToggle = { viewModel.toggleXYLock() },
                onZWLockToggle = { viewModel.toggleZWLock() },
                onCrossSectionToggle = { viewModel.toggleCrossSection() },
                onCrossSectionPositionChange = { viewModel.setCrossSectionPosition(it) },
                onMotionToggle = { viewModel.toggleMotionControl() },
                onMotionSensitivityChange = { viewModel.setMotionSensitivity(it) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
} 