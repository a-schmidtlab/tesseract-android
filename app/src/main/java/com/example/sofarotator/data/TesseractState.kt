package com.example.sofarotator.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * Visualization modes for the tesseract
 */
enum class VisualizationMode {
    WIREFRAME,
    SOLID
}

/**
 * State data class that holds all tesseract visualization parameters
 */
@Stable
data class TesseractViewState(
    // Rotation angles for each 4D plane
    val xwRotation: Double = 0.0,
    val yzRotation: Double = 0.0,
    val xyRotation: Double = 0.0,
    val zwRotation: Double = 0.0,
    
    // Rotation speeds for each plane
    val xwSpeed: Double = 1.0,
    val yzSpeed: Double = 0.7,
    val xySpeed: Double = 0.5,
    val zwSpeed: Double = 0.3,
    
    // Manual slider positions (used when auto-rotation is off)
    val xwSliderPosition: Double = 0.5,
    val yzSliderPosition: Double = 0.35,
    val xySliderPosition: Double = 0.25,
    val zwSliderPosition: Double = 0.15,
    
    // Auto-rotation settings
    val isAutoRotating: Boolean = true,
    val autoRotationSpeed: Double = 1.0,
    
    // UI control states
    val showControls: Boolean = false,
    
    // Visualization settings
    val visualizationMode: VisualizationMode = VisualizationMode.WIREFRAME,
    val projectionMode: ProjectionMode = ProjectionMode.PERSPECTIVE,
    
    // Axis locks
    val lockXW: Boolean = false,
    val lockYZ: Boolean = false,
    val lockXY: Boolean = false,
    val lockZW: Boolean = false,
    
    // Cross-section settings
    val showCrossSection: Boolean = false,
    val crossSectionPosition: Double = 0.0,
    
    // Motion control
    val motionEnabled: Boolean = false,
    val motionSensitivity: Double = 1.0
)

/**
 * ViewModel for managing tesseract state and animations
 */
class TesseractViewModel : ViewModel() {
    
    private val _state = mutableStateOf(TesseractViewState())
    val state: State<TesseractViewState> = _state
    
    /**
     * Update rotation angles based on delta time for smooth animation
     */
    fun updateRotations(deltaTime: Float) {
        val currentState = _state.value
        val rotationDelta = 0.02 * deltaTime * 60 // Scale by delta time and target 60fps
        
        if (currentState.isAutoRotating) {
            // Auto rotation mode - use speed values
            _state.value = currentState.copy(
                xwRotation = if (!currentState.lockXW) {
                    currentState.xwRotation + rotationDelta * currentState.xwSpeed * currentState.autoRotationSpeed
                } else currentState.xwRotation,
                
                yzRotation = if (!currentState.lockYZ) {
                    currentState.yzRotation + rotationDelta * currentState.yzSpeed * currentState.autoRotationSpeed
                } else currentState.yzRotation,
                
                xyRotation = if (!currentState.lockXY) {
                    currentState.xyRotation + rotationDelta * currentState.xySpeed * currentState.autoRotationSpeed
                } else currentState.xyRotation,
                
                zwRotation = if (!currentState.lockZW) {
                    currentState.zwRotation + rotationDelta * currentState.zwSpeed * currentState.autoRotationSpeed
                } else currentState.zwRotation
            )
        } else {
            // Manual mode - use slider positions
            _state.value = currentState.copy(
                xwRotation = currentState.xwRotation + rotationDelta * currentState.xwSliderPosition * 2,
                yzRotation = currentState.yzRotation + rotationDelta * currentState.yzSliderPosition * 2,
                xyRotation = currentState.xyRotation + rotationDelta * currentState.xySliderPosition * 2,
                zwRotation = currentState.zwRotation + rotationDelta * currentState.zwSliderPosition * 2
            )
        }
    }
    
    /**
     * Toggle auto-rotation on/off
     */
    fun toggleAutoRotation() {
        _state.value = _state.value.copy(
            isAutoRotating = !_state.value.isAutoRotating
        )
    }
    
    /**
     * Toggle controls panel visibility
     */
    fun toggleControls() {
        _state.value = _state.value.copy(
            showControls = !_state.value.showControls
        )
    }
    
    /**
     * Set visualization mode (wireframe/solid)
     */
    fun setVisualizationMode(mode: VisualizationMode) {
        _state.value = _state.value.copy(
            visualizationMode = mode
        )
    }
    
    /**
     * Set projection mode (perspective/orthographic)
     */
    fun setProjectionMode(mode: ProjectionMode) {
        _state.value = _state.value.copy(
            projectionMode = mode
        )
    }
    
    /**
     * Set auto-rotation speed
     */
    fun setAutoRotationSpeed(speed: Double) {
        _state.value = _state.value.copy(
            autoRotationSpeed = speed
        )
    }
    
    /**
     * Set individual rotation speed for XW plane
     */
    fun setXWSpeed(speed: Double) {
        _state.value = _state.value.copy(
            xwSpeed = speed
        )
    }
    
    /**
     * Set individual rotation speed for YZ plane
     */
    fun setYZSpeed(speed: Double) {
        _state.value = _state.value.copy(
            yzSpeed = speed
        )
    }
    
    /**
     * Set individual rotation speed for XY plane
     */
    fun setXYSpeed(speed: Double) {
        _state.value = _state.value.copy(
            xySpeed = speed
        )
    }
    
    /**
     * Set individual rotation speed for ZW plane
     */
    fun setZWSpeed(speed: Double) {
        _state.value = _state.value.copy(
            zwSpeed = speed
        )
    }
    
    /**
     * Set manual slider position for XW plane
     */
    fun setXWSliderPosition(position: Double) {
        _state.value = _state.value.copy(
            xwSliderPosition = position
        )
    }
    
    /**
     * Set manual slider position for YZ plane
     */
    fun setYZSliderPosition(position: Double) {
        _state.value = _state.value.copy(
            yzSliderPosition = position
        )
    }
    
    /**
     * Set manual slider position for XY plane
     */
    fun setXYSliderPosition(position: Double) {
        _state.value = _state.value.copy(
            xySliderPosition = position
        )
    }
    
    /**
     * Set manual slider position for ZW plane
     */
    fun setZWSliderPosition(position: Double) {
        _state.value = _state.value.copy(
            zwSliderPosition = position
        )
    }
    
    /**
     * Toggle axis lock for XW plane
     */
    fun toggleXWLock() {
        _state.value = _state.value.copy(
            lockXW = !_state.value.lockXW
        )
    }
    
    /**
     * Toggle axis lock for YZ plane
     */
    fun toggleYZLock() {
        _state.value = _state.value.copy(
            lockYZ = !_state.value.lockYZ
        )
    }
    
    /**
     * Toggle axis lock for XY plane
     */
    fun toggleXYLock() {
        _state.value = _state.value.copy(
            lockXY = !_state.value.lockXY
        )
    }
    
    /**
     * Toggle axis lock for ZW plane
     */
    fun toggleZWLock() {
        _state.value = _state.value.copy(
            lockZW = !_state.value.lockZW
        )
    }
    
    /**
     * Toggle cross-section visibility
     */
    fun toggleCrossSection() {
        _state.value = _state.value.copy(
            showCrossSection = !_state.value.showCrossSection
        )
    }
    
    /**
     * Set cross-section position
     */
    fun setCrossSectionPosition(position: Double) {
        _state.value = _state.value.copy(
            crossSectionPosition = position
        )
    }
    
    /**
     * Reset all rotations to zero
     */
    fun resetRotations() {
        _state.value = _state.value.copy(
            xwRotation = 0.0,
            yzRotation = 0.0,
            xyRotation = 0.0,
            zwRotation = 0.0
        )
    }
    
    /**
     * Reset all speeds to default values
     */
    fun resetSpeeds() {
        _state.value = _state.value.copy(
            xwSpeed = 1.0,
            yzSpeed = 0.7,
            xySpeed = 0.5,
            zwSpeed = 0.3,
            autoRotationSpeed = 1.0
        )
    }
    
    /**
     * Get current rotation state for math calculations
     */
    fun getRotationState(): RotationState {
        val state = _state.value
        return RotationState(
            xw = state.xwRotation,
            yz = state.yzRotation,
            xy = state.xyRotation,
            zw = state.zwRotation
        )
    }
    
    /**
     * Toggle motion control on/off
     */
    fun toggleMotionControl() {
        _state.value = _state.value.copy(
            motionEnabled = !_state.value.motionEnabled
        )
    }
    
    /**
     * Set motion control sensitivity
     */
    fun setMotionSensitivity(sensitivity: Double) {
        _state.value = _state.value.copy(
            motionSensitivity = sensitivity.coerceIn(0.1, 3.0)
        )
    }
    
    /**
     * Apply motion input from device sensors
     * Maps gyroscope data to tesseract rotation
     */
    fun applyMotionInput(x: Double, y: Double, z: Double) {
        val currentState = _state.value
        
        if (currentState.motionEnabled && !currentState.isAutoRotating) {
            val sensitivity = currentState.motionSensitivity * 0.1
            val rotationDelta = 0.016 // ~60fps timing
            
            // Map gyroscope axes to tesseract planes
            _state.value = currentState.copy(
                xwRotation = if (!currentState.lockXW) {
                    currentState.xwRotation + x * sensitivity * rotationDelta
                } else currentState.xwRotation,
                
                yzRotation = if (!currentState.lockYZ) {
                    currentState.yzRotation + y * sensitivity * rotationDelta
                } else currentState.yzRotation,
                
                xyRotation = if (!currentState.lockXY) {
                    currentState.xyRotation + z * sensitivity * rotationDelta
                } else currentState.xyRotation,
                
                // ZW plane can be controlled by combination of movements
                zwRotation = if (!currentState.lockZW) {
                    currentState.zwRotation + (x + y) * 0.5 * sensitivity * rotationDelta
                } else currentState.zwRotation
            )
        }
    }
} 