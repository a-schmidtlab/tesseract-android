package com.example.sofarotator.domain.math

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.*

// ============================================================================
// BASIC DATA STRUCTURES
// ============================================================================

/**
 * A point in 4D space
 */
data class Point4D(val x: Double, val y: Double, val z: Double, val w: Double)

/**
 * A point on the screen with depth information
 */
data class ScreenPoint(val position: Offset, val depth: Double)

/**
 * How much to rotate in each 4D plane
 */
data class Rotation4D(
    val xw: Double = 0.0,  // Rotate in XW plane
    val yz: Double = 0.0,  // Rotate in YZ plane  
    val xy: Double = 0.0,  // Rotate in XY plane
    val zw: Double = 0.0   // Rotate in ZW plane
)

/**
 * The complete tesseract ready for drawing
 */
data class Tesseract(
    val innerCube: List<ScreenPoint>,
    val outerCube: List<ScreenPoint>
)

/**
 * How to project 4D to 2D
 */
enum class ProjectionMode {
    PERSPECTIVE,    // Objects get smaller with distance
    ORTHOGRAPHIC   // Objects stay same size regardless of distance
}

// ============================================================================
// MAIN TESSERACT CREATION
// ============================================================================

object TesseractMath {
    
    /**
     * Create a complete tesseract ready for drawing
     * This is the main function you call to get your tesseract
     */
    fun createTesseract(
        rotation: Rotation4D,
        projectionMode: ProjectionMode,
        screenSize: Size,
        scale: Double
    ): Tesseract {
        // Step 1: Create two 3D cubes at different w-coordinates
        val innerCube = createCube(scale = 0.5, wPosition = 0.5)
        val outerCube = createCube(scale = 1.0, wPosition = -0.5)
        
        // Step 2: Rotate both cubes in 4D space
        val rotatedInner = rotateCube(innerCube, rotation)
        val rotatedOuter = rotateCube(outerCube, rotation)
        
        // Step 3: Project from 4D to 2D screen coordinates
        val screenInner = projectToScreen(rotatedInner, projectionMode, screenSize, scale)
        val screenOuter = projectToScreen(rotatedOuter, projectionMode, screenSize, scale)
        
        return Tesseract(screenInner, screenOuter)
    }
    
    // ============================================================================
    // CUBE CREATION
    // ============================================================================
    
    /**
     * Create the 8 corners of a 3D cube at a specific w-coordinate
     */
    private fun createCube(scale: Double, wPosition: Double): List<Point4D> {
        return listOf(
            // Front face of cube (z = -scale)
            Point4D(-scale, -scale, -scale, wPosition),  // 0: bottom-left-front
            Point4D( scale, -scale, -scale, wPosition),  // 1: bottom-right-front
            Point4D( scale,  scale, -scale, wPosition),  // 2: top-right-front
            Point4D(-scale,  scale, -scale, wPosition),  // 3: top-left-front
            
            // Back face of cube (z = +scale)
            Point4D(-scale, -scale,  scale, wPosition),  // 4: bottom-left-back
            Point4D( scale, -scale,  scale, wPosition),  // 5: bottom-right-back
            Point4D( scale,  scale,  scale, wPosition),  // 6: top-right-back
            Point4D(-scale,  scale,  scale, wPosition)   // 7: top-left-back
        )
    }
    
    // ============================================================================
    // 4D ROTATION
    // ============================================================================
    
    /**
     * Rotate all points in a cube through 4D space
     */
    private fun rotateCube(cube: List<Point4D>, rotation: Rotation4D): List<Point4D> {
        return cube.map { point -> rotatePoint(point, rotation) }
    }
    
    /**
     * Rotate a single point through 4D space
     */
    private fun rotatePoint(point: Point4D, rotation: Rotation4D): Point4D {
        var x = point.x
        var y = point.y
        var z = point.z
        var w = point.w
        
        // Rotate in each 4D plane (same order as Swift app)
        val (newX, newW) = rotate2D(x, w, rotation.xw)
        val (newY, newZ) = rotate2D(y, z, rotation.yz)
        val (finalX, finalY) = rotate2D(newX, newY, rotation.xy)
        val (finalZ, finalW) = rotate2D(newZ, newW, rotation.zw)
        
        return Point4D(finalX, finalY, finalZ, finalW)
    }
    
    /**
     * Rotate two coordinates around each other
     */
    private fun rotate2D(x: Double, y: Double, angle: Double): Pair<Double, Double> {
        val cos = cos(angle)
        val sin = sin(angle)
        return Pair(
            x * cos - y * sin,
            x * sin + y * cos
        )
    }
    
    // ============================================================================
    // PROJECTION TO SCREEN
    // ============================================================================
    
    /**
     * Convert 4D points to 2D screen coordinates
     */
    private fun projectToScreen(
        points: List<Point4D>, 
        mode: ProjectionMode, 
        screenSize: Size, 
        scale: Double
    ): List<ScreenPoint> {
        return points.map { point -> projectSinglePoint(point, mode, screenSize, scale) }
    }
    
    /**
     * Convert one 4D point to a 2D screen coordinate
     */
    private fun projectSinglePoint(
        point: Point4D, 
        mode: ProjectionMode, 
        screenSize: Size, 
        scale: Double
    ): ScreenPoint {
        // Step 1: 4D → 3D (stereographic projection)
        val distance4D = 5.0
        val w1 = 1.0 / (distance4D - point.w)
        val x3D = point.x * w1
        val y3D = point.y * w1
        val z3D = point.z * w1
        
        // Step 2: 3D → 2D (perspective or orthographic)
        val distance3D = 4.0
        val (x2D, y2D, depth) = when (mode) {
            ProjectionMode.PERSPECTIVE -> {
                val perspective = 1.0 / (distance3D - z3D)
                Triple(x3D * perspective, y3D * perspective, perspective)
            }
            ProjectionMode.ORTHOGRAPHIC -> {
                Triple(x3D * 0.5, y3D * 0.5, 1.0 / (distance3D - z3D))
            }
        }
        
        // Step 3: Convert to screen coordinates
        val centerX = screenSize.width / 2f
        val centerY = screenSize.height / 2f
        val screenPosition = Offset(
            x = (centerX + x2D * scale).toFloat(),
            y = (centerY + y2D * scale).toFloat()
        )
        
        return ScreenPoint(screenPosition, depth)
    }
    
    // ============================================================================
    // HELPER FUNCTIONS FOR DRAWING
    // ============================================================================
    
    /**
     * Get which corners of a cube should be connected with lines
     */
    fun getCubeEdges(): List<Pair<Int, Int>> {
        return listOf(
            // Front face edges
            Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 0),
            // Back face edges  
            Pair(4, 5), Pair(5, 6), Pair(6, 7), Pair(7, 4),
            // Front-to-back edges
            Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)
        )
    }
    
    /**
     * Get the faces of a cube for solid rendering
     */
    fun getCubeFaces(): List<List<Int>> {
        return listOf(
            listOf(0, 1, 2, 3), // Front face
            listOf(4, 5, 6, 7), // Back face
            listOf(0, 1, 5, 4), // Bottom face
            listOf(2, 3, 7, 6), // Top face
            listOf(0, 3, 7, 4), // Left face
            listOf(1, 2, 6, 5)  // Right face
        )
    }
    
    /**
     * Calculate how opaque something should be based on its depth
     */
    fun calculateOpacity(depth: Double): Float {
        return (0.2 + depth * 2.0).coerceIn(0.2, 1.0).toFloat()
    }
} 