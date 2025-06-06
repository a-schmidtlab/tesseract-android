package com.example.sofarotator.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.*

/**
 * 4D Vector representation
 */
data class Vector4D(val x: Double, val y: Double, val z: Double, val w: Double) {
    operator fun plus(other: Vector4D) = Vector4D(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Vector4D) = Vector4D(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun times(scalar: Double) = Vector4D(x * scalar, y * scalar, z * scalar, w * scalar)
}

/**
 * 3D Vector representation
 */
data class Vector3D(val x: Double, val y: Double, val z: Double) {
    val magnitude: Double get() = sqrt(x * x + y * y + z * z)
}

/**
 * Rotation state for all 4D planes
 */
data class RotationState(
    val xw: Double = 0.0,
    val yz: Double = 0.0,
    val xy: Double = 0.0,
    val zw: Double = 0.0
)

/**
 * Vertex data for rendering
 */
data class VertexData(
    val point: Offset,
    val depth: Double
)

/**
 * Projection modes
 */
enum class ProjectionMode {
    PERSPECTIVE,
    ORTHOGRAPHIC
}

/**
 * Core 4D mathematics for tesseract manipulation
 * Based on the Swift TesseractView implementation
 */
object TesseractMath {
    
    /**
     * Create vertices for a 3D cube at a specific w-coordinate
     * This matches the Swift implementation approach
     */
    fun createCubeVertices(scale: Double, w: Double): List<Vector4D> {
        return listOf(
            Vector4D(-scale, -scale, -scale, w), Vector4D(scale, -scale, -scale, w),
            Vector4D(scale, scale, -scale, w), Vector4D(-scale, scale, -scale, w),
            Vector4D(-scale, -scale, scale, w), Vector4D(scale, -scale, scale, w),
            Vector4D(scale, scale, scale, w), Vector4D(-scale, scale, scale, w)
        )
    }
    
    /**
     * Apply 4D rotations to a 4D point using the Swift approach
     * This performs rotation in each 4D plane separately
     */
    fun apply4DRotations(
        x: Double, y: Double, z: Double, w: Double,
        rotations: RotationState
    ): Vector4D {
        // Apply rotations in the same order as Swift
        var (rotX, rotW) = rotate2D(x, w, rotations.xw)
        var (rotY, rotZ) = rotate2D(y, z, rotations.yz)
        val (newX, newY) = rotate2D(rotX, rotY, rotations.xy)
        val (newZ, newW) = rotate2D(rotZ, rotW, rotations.zw)
        
        return Vector4D(newX, newY, newZ, newW)
    }
    
    /**
     * 2D rotation in a plane (matches Swift rotate2D function)
     */
    private fun rotate2D(x: Double, y: Double, angle: Double): Pair<Double, Double> {
        val cosA = cos(angle)
        val sinA = sin(angle)
        return Pair(
            x * cosA - y * sinA,
            x * sinA + y * cosA
        )
    }
    
    /**
     * Apply rotations to all vertices in a cube
     */
    fun applyRotations(vertices: List<Vector4D>, rotations: RotationState): List<Vector4D> {
        return vertices.map { vertex ->
            apply4DRotations(vertex.x, vertex.y, vertex.z, vertex.w, rotations)
        }
    }
    
    /**
     * Project 4D point to 2D using stereographic projection
     * This matches the Swift project4D function exactly
     */
    fun project4DToScreen(
        vertex4D: Vector4D,
        projectionMode: ProjectionMode,
        screenSize: Size,
        scale: Double
    ): VertexData {
        // Stereographic projection: 4D → 3D
        val distance = 5.0
        val w1 = 1.0 / (distance - vertex4D.w)
        val projX = vertex4D.x * w1
        val projY = vertex4D.y * w1
        val projZ = vertex4D.z * w1
        
        // View distance for both projection modes
        val viewDistance = 4.0
        val screenX: Double
        val screenY: Double
        val depth: Double
        
        when (projectionMode) {
            ProjectionMode.PERSPECTIVE -> {
                // Perspective projection: 3D → 2D with depth effect
                val perspective = 1.0 / (viewDistance - projZ)
                screenX = projX * perspective
                screenY = projY * perspective
                depth = perspective
            }
            ProjectionMode.ORTHOGRAPHIC -> {
                // Orthographic projection: 3D → 2D without depth distortion
                screenX = projX * 0.5 // Scale factor for orthographic view
                screenY = projY * 0.5
                depth = 1.0 / (viewDistance - projZ) // Keep depth for shading
            }
        }
        
        val centerX = screenSize.width / 2f
        val centerY = screenSize.height / 2f
        
        return VertexData(
            point = Offset(
                x = (centerX + screenX * scale).toFloat(),
                y = (centerY + screenY * scale).toFloat() // Note: No Y flip like Swift since Android coords are different
            ),
            depth = depth
        )
    }
    
    /**
     * Calculate opacity based on depth (closer objects are more opaque)
     */
    fun calculateOpacity(depth: Double): Float {
        return (0.2 + depth * 2.0).coerceIn(0.2, 1.0).toFloat()
    }
    
    /**
     * Get edges that define the wireframe of a cube
     * Returns pairs of vertex indices that should be connected
     */
    fun getCubeEdges(): List<Pair<Int, Int>> {
        return listOf(
            // Front face (z = -scale)
            Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 0),
            // Back face (z = scale)
            Pair(4, 5), Pair(5, 6), Pair(6, 7), Pair(7, 4),
            // Connecting edges between front and back
            Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)
        )
    }
    
    /**
     * Get faces that define the solid rendering of a cube
     * Returns lists of vertex indices for each face (in clockwise order)
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
     * Create the complete tesseract data structure
     * Returns inner cube, outer cube vertices for rendering
     */
    fun createTesseractData(
        rotations: RotationState,
        projectionMode: ProjectionMode,
        screenSize: Size,
        scale: Double
    ): TesseractData {
        // Create inner and outer cubes like in Swift
        val innerCubeScale = 0.5
        val outerCubeScale = 1.0
        val innerW = 0.5
        val outerW = -0.5
        
        // Create cube vertices
        val innerCube = createCubeVertices(innerCubeScale, innerW)
        val outerCube = createCubeVertices(outerCubeScale, outerW)
        
        // Apply rotations
        val rotatedInnerCube = applyRotations(innerCube, rotations)
        val rotatedOuterCube = applyRotations(outerCube, rotations)
        
        // Project to screen coordinates
        val projectedInnerCube = rotatedInnerCube.map { vertex ->
            project4DToScreen(vertex, projectionMode, screenSize, scale)
        }
        val projectedOuterCube = rotatedOuterCube.map { vertex ->
            project4DToScreen(vertex, projectionMode, screenSize, scale)
        }
        
        return TesseractData(projectedInnerCube, projectedOuterCube)
    }
}

/**
 * Data structure containing the projected tesseract vertices
 */
data class TesseractData(
    val innerCube: List<VertexData>,
    val outerCube: List<VertexData>
) 