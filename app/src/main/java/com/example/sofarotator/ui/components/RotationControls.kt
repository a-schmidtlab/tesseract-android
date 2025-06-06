package com.example.sofarotator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sofarotator.data.TesseractViewState
import com.example.sofarotator.data.VisualizationMode
import com.example.sofarotator.data.ProjectionMode

/**
 * Advanced rotation controls panel
 * Matches the iOS TesseractControlsView functionality
 */
@Composable
fun RotationControlsPanel(
    state: TesseractViewState,
    onVisualizationModeChange: (VisualizationMode) -> Unit,
    onProjectionModeChange: (ProjectionMode) -> Unit,
    onAutoRotationToggle: () -> Unit,
    onAutoRotationSpeedChange: (Double) -> Unit,
    onXWSpeedChange: (Double) -> Unit,
    onYZSpeedChange: (Double) -> Unit,
    onXYSpeedChange: (Double) -> Unit,
    onZWSpeedChange: (Double) -> Unit,
    onXWSliderChange: (Double) -> Unit,
    onYZSliderChange: (Double) -> Unit,
    onXYSliderChange: (Double) -> Unit,
    onZWSliderChange: (Double) -> Unit,
    onXWLockToggle: () -> Unit,
    onYZLockToggle: () -> Unit,
    onXYLockToggle: () -> Unit,
    onZWLockToggle: () -> Unit,
    onCrossSectionToggle: () -> Unit,
    onCrossSectionPositionChange: (Double) -> Unit,
    onMotionToggle: () -> Unit,
    onMotionSensitivityChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Text(
                text = "Tesseract Controls",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Cyan,
                fontWeight = FontWeight.Bold
            )
            
            Divider(color = Color.Cyan.copy(alpha = 0.3f))
            
            // Visualization Mode Section
            VisualizationModeSection(
                currentMode = state.visualizationMode,
                onModeChange = onVisualizationModeChange
            )
            
            // Projection Mode Section
            ProjectionModeSection(
                currentMode = state.projectionMode,
                onModeChange = onProjectionModeChange
            )
            
            Divider(color = Color.Blue.copy(alpha = 0.3f))
            
            // Auto-rotation Section
            AutoRotationSection(
                isAutoRotating = state.isAutoRotating,
                autoRotationSpeed = state.autoRotationSpeed,
                onToggle = onAutoRotationToggle,
                onSpeedChange = onAutoRotationSpeedChange
            )
            
            Divider(color = Color.Blue.copy(alpha = 0.3f))
            
            // 4D Rotation Planes Section
            Text(
                text = "4D Rotation Planes",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Cyan,
                fontWeight = FontWeight.Bold
            )
            
            // XW Plane Control
            RotationPlaneControl(
                label = "XW Plane",
                description = "Rotation in X-W dimensional plane",
                speed = state.xwSpeed,
                sliderPosition = state.xwSliderPosition,
                isLocked = state.lockXW,
                isAutoRotating = state.isAutoRotating,
                onSpeedChange = onXWSpeedChange,
                onSliderChange = onXWSliderChange,
                onLockToggle = onXWLockToggle,
                color = Color.Cyan
            )
            
            // YZ Plane Control
            RotationPlaneControl(
                label = "YZ Plane",
                description = "Rotation in Y-Z dimensional plane",
                speed = state.yzSpeed,
                sliderPosition = state.yzSliderPosition,
                isLocked = state.lockYZ,
                isAutoRotating = state.isAutoRotating,
                onSpeedChange = onYZSpeedChange,
                onSliderChange = onYZSliderChange,
                onLockToggle = onYZLockToggle,
                color = Color.Blue
            )
            
            // XY Plane Control
            RotationPlaneControl(
                label = "XY Plane",
                description = "Rotation in X-Y dimensional plane (standard 3D)",
                speed = state.xySpeed,
                sliderPosition = state.xySliderPosition,
                isLocked = state.lockXY,
                isAutoRotating = state.isAutoRotating,
                onSpeedChange = onXYSpeedChange,
                onSliderChange = onXYSliderChange,
                onLockToggle = onXYLockToggle,
                color = Color.Green
            )
            
            // ZW Plane Control
            RotationPlaneControl(
                label = "ZW Plane",
                description = "Rotation in Z-W dimensional plane",
                speed = state.zwSpeed,
                sliderPosition = state.zwSliderPosition,
                isLocked = state.lockZW,
                isAutoRotating = state.isAutoRotating,
                onSpeedChange = onZWSpeedChange,
                onSliderChange = onZWSliderChange,
                onLockToggle = onZWLockToggle,
                color = Color(0xFF9C27B0) // Purple
            )
            
            Divider(color = Color(0xFF9C27B0).copy(alpha = 0.3f))
            
            // Cross-section Section
            CrossSectionSection(
                showCrossSection = state.showCrossSection,
                position = state.crossSectionPosition,
                onToggle = onCrossSectionToggle,
                onPositionChange = onCrossSectionPositionChange
            )
            
            Divider(color = Color.Green.copy(alpha = 0.3f))
            
            // Motion Controls Section
            MotionControlSection(
                motionEnabled = state.motionEnabled,
                motionSensitivity = state.motionSensitivity,
                onMotionToggle = onMotionToggle,
                onSensitivityChange = onMotionSensitivityChange
            )
        }
    }
}

@Composable
private fun VisualizationModeSection(
    currentMode: VisualizationMode,
    onModeChange: (VisualizationMode) -> Unit
) {
    Column {
        Text(
            text = "Visualization Mode",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onModeChange(VisualizationMode.WIREFRAME) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentMode == VisualizationMode.WIREFRAME) Color.Cyan else Color.Transparent
                )
            ) {
                Text(
                    "Wireframe",
                    color = if (currentMode == VisualizationMode.WIREFRAME) Color.Black else Color.White
                )
            }
            
            Button(
                onClick = { onModeChange(VisualizationMode.SOLID) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentMode == VisualizationMode.SOLID) Color.Blue else Color.Transparent
                )
            ) {
                Text(
                    "Solid",
                    color = if (currentMode == VisualizationMode.SOLID) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
private fun ProjectionModeSection(
    currentMode: ProjectionMode,
    onModeChange: (ProjectionMode) -> Unit
) {
    Column {
        Text(
            text = "Projection Mode",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onModeChange(ProjectionMode.PERSPECTIVE) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentMode == ProjectionMode.PERSPECTIVE) Color.Cyan else Color.Transparent
                )
            ) {
                Text(
                    "Perspective",
                    color = if (currentMode == ProjectionMode.PERSPECTIVE) Color.Black else Color.White
                )
            }
            
            Button(
                onClick = { onModeChange(ProjectionMode.ORTHOGRAPHIC) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentMode == ProjectionMode.ORTHOGRAPHIC) Color.Cyan else Color.Transparent
                )
            ) {
                Text(
                    "Orthographic",
                    color = if (currentMode == ProjectionMode.ORTHOGRAPHIC) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
private fun AutoRotationSection(
    isAutoRotating: Boolean,
    autoRotationSpeed: Double,
    onToggle: () -> Unit,
    onSpeedChange: (Double) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Auto-Rotation",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            
            Switch(
                checked = isAutoRotating,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green,
                    checkedTrackColor = Color.Green.copy(alpha = 0.5f)
                )
            )
        }
        
        if (isAutoRotating) {
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Global Speed: ${String.format("%.1f", autoRotationSpeed)}×",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Slider(
                value = autoRotationSpeed.toFloat(),
                onValueChange = { onSpeedChange(it.toDouble()) },
                valueRange = 0f..3f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Green,
                    activeTrackColor = Color.Green.copy(alpha = 0.7f),
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun RotationPlaneControl(
    label: String,
    description: String,
    speed: Double,
    sliderPosition: Double,
    isLocked: Boolean,
    isAutoRotating: Boolean,
    onSpeedChange: (Double) -> Unit,
    onSliderChange: (Double) -> Unit,
    onLockToggle: () -> Unit,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(color.copy(alpha = 0.3f))
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Speed/Position indicator
                    Text(
                        text = if (isLocked) "Locked" else if (isAutoRotating) {
                            String.format("%.1f×", speed)
                        } else {
                            String.format("%.1f", sliderPosition)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isLocked) Color.Red else color
                    )
                    
                    // Lock toggle
                    IconButton(
                        onClick = onLockToggle,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = if (isLocked) "Unlock" else "Lock",
                            tint = if (isLocked) Color.Red else color,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Slider
            if (!isLocked) {
                if (isAutoRotating) {
                    // Speed control for auto-rotation
                    Text(
                        text = "Rotation Speed",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    
                    Slider(
                        value = speed.toFloat(),
                        onValueChange = { onSpeedChange(it.toDouble()) },
                        valueRange = -2f..2f,
                        colors = SliderDefaults.colors(
                            thumbColor = color,
                            activeTrackColor = color.copy(alpha = 0.7f),
                            inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    // Manual position control
                    Text(
                        text = "Manual Control",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    
                    Slider(
                        value = sliderPosition.toFloat(),
                        onValueChange = { onSliderChange(it.toDouble()) },
                        valueRange = -1f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = color,
                            activeTrackColor = color.copy(alpha = 0.7f),
                            inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CrossSectionSection(
    showCrossSection: Boolean,
    position: Double,
    onToggle: () -> Unit,
    onPositionChange: (Double) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cross-Section Visualization",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            
            Switch(
                checked = showCrossSection,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF9C27B0),
                    checkedTrackColor = Color(0xFF9C27B0).copy(alpha = 0.5f)
                )
            )
        }
        
        if (showCrossSection) {
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "W-Position: ${String.format("%.2f", position)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9C27B0)
            )
            
            Text(
                text = "Slice through the 4D tesseract at the specified W coordinate",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = position.toFloat(),
                onValueChange = { onPositionChange(it.toDouble()) },
                valueRange = -1f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF9C27B0),
                    activeTrackColor = Color(0xFF9C27B0).copy(alpha = 0.7f),
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun MotionControlSection(
    motionEnabled: Boolean,
    motionSensitivity: Double,
    onMotionToggle: () -> Unit,
    onSensitivityChange: (Double) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Device Motion Control",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = "Control tesseract with device movement",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            Switch(
                checked = motionEnabled,
                onCheckedChange = { onMotionToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green,
                    checkedTrackColor = Color.Green.copy(alpha = 0.5f)
                )
            )
        }
        
        if (motionEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Motion Sensitivity: ${String.format("%.1f", motionSensitivity)}×",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Green
            )
            
            Text(
                text = "Higher values make the tesseract more responsive to device movement",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = motionSensitivity.toFloat(),
                onValueChange = { onSensitivityChange(it.toDouble()) },
                valueRange = 0.1f..3.0f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Green,
                    activeTrackColor = Color.Green.copy(alpha = 0.7f),
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Instructions
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.1f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color.Green.copy(alpha = 0.3f))
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "💡 Motion Instructions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "• Tilt device to control XW/YZ planes\n• Rotate device to control XY/ZW planes\n• Motion works best with auto-rotation OFF",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
} 