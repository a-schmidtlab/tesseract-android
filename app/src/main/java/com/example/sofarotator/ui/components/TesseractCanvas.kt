package com.example.sofarotator.ui.components

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
import com.example.sofarotator.data.TesseractMath
import com.example.sofarotator.data.TesseractViewState
import com.example.sofarotator.data.VertexData
import com.example.sofarotator.data.VisualizationMode

/**
 * Main tesseract rendering canvas
 * Matches the Swift TesseractView implementation
 */
@Composable
fun TesseractCanvas(
    state: TesseractViewState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val scale = minOf(size.width, size.height) * 0.2 // Adjusted scale to match Swift
        
        // Create tesseract data using the new approach
        val rotations = com.example.sofarotator.data.RotationState(
            xw = state.xwRotation,
            yz = state.yzRotation,
            xy = state.xyRotation,
            zw = state.zwRotation
        )
        
        val tesseractData = TesseractMath.createTesseractData(
            rotations = rotations,
            projectionMode = state.projectionMode,
            screenSize = size,
            scale = scale.toDouble()
        )
        
        // Render based on visualization mode
        when (state.visualizationMode) {
            VisualizationMode.WIREFRAME -> drawWireframe(tesseractData.innerCube, tesseractData.outerCube)
            VisualizationMode.SOLID -> drawSolid(tesseractData.innerCube, tesseractData.outerCube)
        }
        
        // Draw cross-section if enabled
        if (state.showCrossSection) {
            drawCrossSection(state.crossSectionPosition, scale.toDouble(), center, rotations)
        }
    }
}

/**
 * Draw tesseract in wireframe mode (matches Swift drawWireframe)
 */
private fun DrawScope.drawWireframe(
    innerCube: List<VertexData>,
    outerCube: List<VertexData>
) {
    // Draw connections between inner and outer cubes first (background)
    drawConnections(innerCube, outerCube)
    
    // Draw outer cube
    drawCubeEdges(outerCube, Color.Blue)
    
    // Draw inner cube (foreground)
    drawCubeEdges(innerCube, Color.Cyan)
}

/**
 * Draw tesseract in solid mode with faces (matches Swift drawSolid)
 */
private fun DrawScope.drawSolid(
    innerCube: List<VertexData>,
    outerCube: List<VertexData>
) {
    // Get face definitions
    val faces = TesseractMath.getCubeFaces()
    
    // Draw outer cube faces first
    drawCubeFaces(outerCube, faces, Color.Blue)
    
    // Draw inner cube faces
    drawCubeFaces(innerCube, faces, Color.Cyan)
    
    // Draw connecting lines between cubes (reduced opacity for solid mode)
    drawConnections(innerCube, outerCube, solidMode = true)
}

/**
 * Draw connections between inner and outer cubes (matches Swift drawConnections)
 */
private fun DrawScope.drawConnections(
    innerCube: List<VertexData>,
    outerCube: List<VertexData>,
    solidMode: Boolean = false
) {
    for (i in 0 until 8) {
        val avgDepth = (innerCube[i].depth + outerCube[i].depth) / 2.0
        val opacity = TesseractMath.calculateOpacity(avgDepth) * if (solidMode) 0.3f else 0.8f
        
        // Create gradient for the connection line
        val gradient = Brush.linearGradient(
            colors = listOf(
                Color.Blue.copy(alpha = opacity * 0.8f),
                Color.Cyan.copy(alpha = opacity * 0.6f)
            ),
            start = innerCube[i].point,
            end = outerCube[i].point
        )
        
        drawLine(
            brush = gradient,
            start = innerCube[i].point,
            end = outerCube[i].point,
            strokeWidth = 2.0f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Draw edges of a cube (matches Swift drawCube)
 */
private fun DrawScope.drawCubeEdges(
    vertices: List<VertexData>,
    baseColor: Color
) {
    val edges = TesseractMath.getCubeEdges()
    
    edges.forEach { (startIdx, endIdx) ->
        val startVertex = vertices[startIdx]
        val endVertex = vertices[endIdx]
        
        // Calculate opacity based on average depth
        val avgDepth = (startVertex.depth + endVertex.depth) / 2.0
        val opacity = TesseractMath.calculateOpacity(avgDepth)
        
        // Create gradient from start to end
        val gradient = Brush.linearGradient(
            colors = listOf(
                baseColor.copy(alpha = opacity),
                baseColor.copy(alpha = opacity * 0.8f)
            ),
            start = startVertex.point,
            end = endVertex.point
        )
        
        drawLine(
            brush = gradient,
            start = startVertex.point,
            end = endVertex.point,
            strokeWidth = 2.0f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Draw faces of a cube for solid rendering
 */
private fun DrawScope.drawCubeFaces(
    vertices: List<VertexData>,
    faces: List<List<Int>>,
    baseColor: Color
) {
    faces.forEach { face ->
        // Calculate average depth for the face
        val avgDepth = face.map { vertices[it].depth }.average()
        val opacity = TesseractMath.calculateOpacity(avgDepth)
        
        // Create path for the face
        val path = Path().apply {
            moveTo(vertices[face[0]].point.x, vertices[face[0]].point.y)
            for (i in 1 until face.size) {
                lineTo(vertices[face[i]].point.x, vertices[face[i]].point.y)
            }
            close()
        }
        
        // Create gradient for the face (matches Swift)
        val gradient = Brush.linearGradient(
            colors = listOf(
                baseColor.copy(alpha = opacity * 0.3f),
                baseColor.copy(alpha = opacity * 0.2f)
            ),
            start = vertices[face[0]].point,
            end = vertices[face[2]].point
        )
        
        // Fill the face
        drawPath(
            path = path,
            brush = gradient
        )
        
        // Draw face outline
        drawPath(
            path = path,
            color = baseColor.copy(alpha = opacity),
            style = Stroke(width = 1.5f)
        )
    }
}

/**
 * Draw cross-section of the tesseract
 */
private fun DrawScope.drawCrossSection(
    position: Double,
    scale: Double,
    center: Offset,
    rotations: com.example.sofarotator.data.RotationState
) {
    // Create vertices for the cross-section at the given w position
    val sectionCube = TesseractMath.createCubeVertices(1.0, position)
    val rotatedSection = TesseractMath.applyRotations(sectionCube, rotations)
    
    // Project to screen coordinates
    val sectionVertices = rotatedSection.map { vertex4D ->
        TesseractMath.project4DToScreen(
            vertex4D,
            com.example.sofarotator.data.ProjectionMode.PERSPECTIVE,
            size,
            scale
        )
    }
    
    // Calculate average depth for the entire section
    val avgDepth = sectionVertices.map { it.depth }.average()
    val opacity = TesseractMath.calculateOpacity(avgDepth)
    
    // Create path for cross-section
    if (sectionVertices.isNotEmpty()) {
        val path = Path().apply {
            moveTo(sectionVertices[0].point.x, sectionVertices[0].point.y)
            sectionVertices.drop(1).forEach { vertex ->
                lineTo(vertex.point.x, vertex.point.y)
            }
            close()
        }
        
        // Draw cross-section with purple gradient (matches Swift)
        val gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27B0).copy(alpha = opacity * 0.3f), // Purple
                Color(0xFFE91E63).copy(alpha = opacity * 0.2f)  // Pink
            ),
            start = center - Offset(scale.toFloat() / 2, scale.toFloat() / 2),
            end = center + Offset(scale.toFloat() / 2, scale.toFloat() / 2)
        )
        
        drawPath(
            path = path,
            brush = gradient
        )
        
        // Draw cross-section outline
        drawPath(
            path = path,
            color = Color(0xFF9C27B0).copy(alpha = opacity),
            style = Stroke(width = 2.0f)
        )
    }
}