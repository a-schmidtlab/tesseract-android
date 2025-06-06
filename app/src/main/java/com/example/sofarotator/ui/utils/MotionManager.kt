package com.example.sofarotator.ui.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Motion manager for device sensor integration
 * Android equivalent of iOS CoreMotion
 */
class MotionManager(private val context: Context) {
    private val sensorManager: SensorManager = 
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    private val rotationSensor: Sensor? = 
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    
    private val gyroscopeSensor: Sensor? = 
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    
    private val _rotation = MutableStateFlow(0.0)
    val rotation: StateFlow<Double> = _rotation.asStateFlow()
    
    private val _pitch = MutableStateFlow(0.0)
    val pitch: StateFlow<Double> = _pitch.asStateFlow()
    
    private val _roll = MutableStateFlow(0.0)
    val roll: StateFlow<Double> = _roll.asStateFlow()
    
    private val _gyroscope = MutableStateFlow(Triple(0.0, 0.0, 0.0))
    val gyroscope: StateFlow<Triple<Double, Double, Double>> = _gyroscope.asStateFlow()
    
    private var isMonitoring = false
    
    private val rotationSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                // Convert rotation vector to rotation matrix
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                
                // Extract orientation angles
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)
                
                // Convert to degrees and update state
                val yawInDegrees = Math.toDegrees(orientation[0].toDouble())
                val pitchInDegrees = Math.toDegrees(orientation[1].toDouble())
                val rollInDegrees = Math.toDegrees(orientation[2].toDouble())
                
                _rotation.value = yawInDegrees
                _pitch.value = pitchInDegrees
                _roll.value = rollInDegrees
            }
        }
        
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes if needed
        }
    }
    
    private val gyroscopeListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                // Raw gyroscope data (rad/s)
                val x = event.values[0].toDouble()
                val y = event.values[1].toDouble()
                val z = event.values[2].toDouble()
                
                _gyroscope.value = Triple(x, y, z)
            }
        }
        
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes if needed
        }
    }
    
    /**
     * Start monitoring device motion sensors
     */
    fun startMonitoring() {
        if (!isMonitoring) {
            rotationSensor?.let { sensor ->
                sensorManager.registerListener(
                    rotationSensorListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_GAME // 60fps for smooth animation
                )
            }
            
            gyroscopeSensor?.let { sensor ->
                sensorManager.registerListener(
                    gyroscopeListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_GAME
                )
            }
            
            isMonitoring = true
        }
    }
    
    /**
     * Stop monitoring device motion sensors
     */
    fun stopMonitoring() {
        if (isMonitoring) {
            sensorManager.unregisterListener(rotationSensorListener)
            sensorManager.unregisterListener(gyroscopeListener)
            isMonitoring = false
        }
    }
    
    /**
     * Check if rotation vector sensor is available
     */
    fun isRotationSensorAvailable(): Boolean {
        return rotationSensor != null
    }
    
    /**
     * Check if gyroscope sensor is available
     */
    fun isGyroscopeAvailable(): Boolean {
        return gyroscopeSensor != null
    }
    
    /**
     * Get normalized rotation value suitable for tesseract rotation
     * Maps device rotation to tesseract rotation speed
     */
    fun getNormalizedRotation(): Double {
        return _rotation.value / 180.0 // Normalize to -1..1 range
    }
    
    /**
     * Get rotation speed based on gyroscope data
     */
    fun getRotationSpeed(): Triple<Double, Double, Double> {
        val (x, y, z) = _gyroscope.value
        // Scale gyroscope values for tesseract rotation
        return Triple(
            x * 0.5, // Scale factor for smooth rotation
            y * 0.5,
            z * 0.5
        )
    }
}

/**
 * Composable function to create and manage MotionManager
 */
@Composable
fun rememberMotionManager(): MotionManager {
    val context = LocalContext.current
    val motionManager = remember { MotionManager(context) }
    
    DisposableEffect(Unit) {
        motionManager.startMonitoring()
        onDispose {
            motionManager.stopMonitoring()
        }
    }
    
    return motionManager
}

/**
 * Composable to collect motion data
 */
@Composable
fun MotionData(
    motionManager: MotionManager,
    onRotationChange: (Double) -> Unit = {},
    onGyroscopeChange: (Triple<Double, Double, Double>) -> Unit = {}
) {
    val rotation by motionManager.rotation.collectAsState()
    val gyroscope by motionManager.gyroscope.collectAsState()
    
    LaunchedEffect(rotation) {
        onRotationChange(rotation)
    }
    
    LaunchedEffect(gyroscope) {
        onGyroscopeChange(gyroscope)
    }
} 