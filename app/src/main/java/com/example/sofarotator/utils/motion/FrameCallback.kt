package com.example.sofarotator.utils.motion

import android.view.Choreographer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

/**
 * Frame callback for smooth animation
 * Android equivalent of iOS CADisplayLink
 */
class FrameCallback(private val onFrame: (deltaTime: Float) -> Unit) {
    private var lastFrameTime = 0L
    private val choreographer = Choreographer.getInstance()
    
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val currentTime = frameTimeNanos / 1_000_000 // Convert to milliseconds
            val deltaTime = if (lastFrameTime == 0L) {
                16f // First frame, assume 60fps
            } else {
                minOf((currentTime - lastFrameTime).toFloat(), 33f) // Cap at 30fps minimum
            }
            lastFrameTime = currentTime
            
            onFrame(deltaTime / 1000f) // Convert to seconds
            choreographer.postFrameCallback(this)
        }
    }
    
    fun start() {
        lastFrameTime = 0L
        choreographer.postFrameCallback(frameCallback)
    }
    
    fun stop() {
        choreographer.removeFrameCallback(frameCallback)
    }
}

/**
 * Composable function to handle frame-based animation
 */
@Composable
fun rememberFrameCallback(
    enabled: Boolean,
    onFrame: (deltaTime: Float) -> Unit
): FrameCallback {
    val callback = remember { FrameCallback(onFrame) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            callback.start()
        } else {
            callback.stop()
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            callback.stop()
        }
    }
    
    return callback
} 