package com.example.sofarotator.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.sofarotator.domain.math.TesseractMath
import com.example.sofarotator.presentation.state.TesseractViewState
import com.example.sofarotator.domain.math.ScreenPoint
import com.example.sofarotator.domain.models.VisualizationMode
import com.example.sofarotator.domain.math.Rotation4D

/**
 * Main tesseract rendering canvas
 * Simple and clear tesseract visualization
 */
@Composable
fun TesseractCanvas(
    state: TesseractViewState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val scale = minOf(size.width, size.height) * 0.2
        
        // Create the tesseract with current rotation settings
        val rotation = Rotation4D(
            xw = state.xwRotation,
            yz = state.yzRotation,
            xy = state.xyRotation,
            zw = state.zwRotation
        )
        
        val tesseract = TesseractMath.createTesseract(
            rotation = rotation,
            projectionMode = state.projectionMode,
            screenSize = size,
            scale = scale.toDouble()
        )
        
        // Draw the tesseract
        when (state.visualizationMode) {
            VisualizationMode.WIREFRAME -> drawWireframe(tesseract)
            VisualizationMode.SOLID -> drawSolid(tesseract)
        }
        
        // Draw cross-section if enabled
        if (state.showCrossSection) {
            drawCrossSection(state.crossSectionPosition, scale.toDouble(), rotation)
        }
    }
}

// ============================================================================
// WIREFRAME DRAWING
// ============================================================================

/**
 * Draw tesseract as wireframe lines
 */
private fun DrawScope.drawWireframe(tesseract: com.example.sofarotator.domain.math.Tesseract) {
    // 1. Draw connections between inner and outer cubes (background)
    drawConnections(tesseract.innerCube, tesseract.outerCube)
    
    // 2. Draw outer cube (middle layer)
    drawCubeEdges(tesseract.outerCube, Color.Blue)
    
    // 3. Draw inner cube (foreground)
    drawCubeEdges(tesseract.innerCube, Color.Cyan)
}

// ============================================================================
// SOLID DRAWING
// ============================================================================

/**
 * Draw tesseract as solid faces
 */
private fun DrawScope.drawSolid(tesseract: com.example.sofarotator.domain.math.Tesseract) {
    val faces = TesseractMath.getCubeFaces()
    
    // 1. Draw outer cube faces (background)
    drawCubeFaces(tesseract.outerCube, faces, Color.Blue)
    
    // 2. Draw inner cube faces (foreground)
    drawCubeFaces(tesseract.innerCube, faces, Color.Cyan)
    
    // 3. Draw connecting lines (subtle)
    drawConnections(tesseract.innerCube, tesseract.outerCube, isSubtle = true)
}

// ============================================================================
// DRAWING HELPER FUNCTIONS
// ============================================================================

/**
 * Draw connections between corresponding corners of inner and outer cubes
 */
private fun DrawScope.drawConnections(
    innerCube: List<ScreenPoint>,
    outerCube: List<ScreenPoint>,
    isSubtle: Boolean = false
) {
    for (i in 0 until 8) {
        val averageDepth = (innerCube[i].depth + outerCube[i].depth) / 2.0
        val opacity = TesseractMath.calculateOpacity(averageDepth) * if (isSubtle) 0.3f else 0.8f
        
        val gradient = Brush.linearGradient(
            colors = listOf(
                Color.Blue.copy(alpha = opacity * 0.8f),
                Color.Cyan.copy(alpha = opacity * 0.6f)
            ),
            start = innerCube[i].position,
            end = outerCube[i].position
        )
        
        drawLine(
            brush = gradient,
            start = innerCube[i].position,
            end = outerCube[i].position,
            strokeWidth = 2.0f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Draw the edges of a cube
 */
private fun DrawScope.drawCubeEdges(
    corners: List<ScreenPoint>,
    color: Color
) {
    val edges = TesseractMath.getCubeEdges()
    
    edges.forEach { (startIndex, endIndex) ->
        val startCorner = corners[startIndex]
        val endCorner = corners[endIndex]
        
        // Calculate how visible this edge should be
        val averageDepth = (startCorner.depth + endCorner.depth) / 2.0
        val opacity = TesseractMath.calculateOpacity(averageDepth)
        
        val gradient = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = opacity),
                color.copy(alpha = opacity * 0.8f)
            ),
            start = startCorner.position,
            end = endCorner.position
        )
        
        drawLine(
            brush = gradient,
            start = startCorner.position,
            end = endCorner.position,
            strokeWidth = 2.0f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Draw the faces of a cube as filled shapes
 */
private fun DrawScope.drawCubeFaces(
    corners: List<ScreenPoint>,
    faceDefinitions: List<List<Int>>,
    color: Color
) {
    faceDefinitions.forEach { face ->
        // Calculate how visible this face should be
        val averageDepth = face.map { corners[it].depth }.average()
        val opacity = TesseractMath.calculateOpacity(averageDepth)
        
        // Create the shape of the face
        val facePath = Path().apply {
            moveTo(corners[face[0]].position.x, corners[face[0]].position.y)
            for (i in 1 until face.size) {
                lineTo(corners[face[i]].position.x, corners[face[i]].position.y)
            }
            close()
        }
        
        // Fill the face with gradient
        val gradient = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = opacity * 0.3f),
                color.copy(alpha = opacity * 0.2f)
            ),
            start = corners[face[0]].position,
            end = corners[face[2]].position
        )
        
        drawPath(path = facePath, brush = gradient)
        
        // Draw face outline
        drawPath(
            path = facePath,
            color = color.copy(alpha = opacity),
            style = Stroke(width = 1.5f)
        )
    }
}

// ============================================================================
// CROSS-SECTION DRAWING
// ============================================================================

/**
 * Draw a cross-section of the tesseract at a specific w-coordinate
 */
private fun DrawScope.drawCrossSection(
    wPosition: Double,
    scale: Double,
    rotation: Rotation4D
) {
    // Create a single cube at the cross-section position
    val sectionCube = TesseractMath.createTesseract(
        rotation = rotation,
        projectionMode = com.example.sofarotator.domain.math.ProjectionMode.PERSPECTIVE,
        screenSize = size,
        scale = scale
    )
    
    // Use the inner cube as our cross-section (simplified approach)
    val crossSectionCorners = sectionCube.innerCube
    
    if (crossSectionCorners.isNotEmpty()) {
        // Calculate visibility
        val averageDepth = crossSectionCorners.map { it.depth }.average()
        val opacity = TesseractMath.calculateOpacity(averageDepth)
        
        // Create cross-section shape
        val crossSectionPath = Path().apply {
            moveTo(crossSectionCorners[0].position.x, crossSectionCorners[0].position.y)
            crossSectionCorners.drop(1).forEach { corner ->
                lineTo(corner.position.x, corner.position.y)
            }
            close()
        }
        
        // Draw with purple gradient
        val center = Offset(size.width / 2f, size.height / 2f)
        val gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27B0).copy(alpha = opacity * 0.3f), // Purple
                Color(0xFFE91E63).copy(alpha = opacity * 0.2f)  // Pink
            ),
            start = center - Offset(scale.toFloat() / 2, scale.toFloat() / 2),
            end = center + Offset(scale.toFloat() / 2, scale.toFloat() / 2)
        )
        
        drawPath(path = crossSectionPath, brush = gradient)
        
        // Draw outline
        drawPath(
            path = crossSectionPath,
            color = Color(0xFF9C27B0).copy(alpha = opacity),
            style = Stroke(width = 2.0f)
        )
    }
}