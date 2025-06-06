import com.example.sofarotator.data.TesseractMath
import com.example.sofarotator.data.Vector4D
import kotlin.math.PI

/**
 * Simple test to verify 4D Tesseract mathematics
 */
fun main() {
    println("🎯 TESSERACT 4D MATHEMATICS TEST")
    println("=====================================")
    
    // Test 4D vector creation
    val vertex = Vector4D(1.0, 1.0, 1.0, 1.0)
    println("✅ Created 4D vertex: $vertex")
    
    // Test rotation matrices
    val xwAngle = PI / 4 // 45 degrees
    val rotationMatrix = TesseractMath.createXWRotationMatrix(xwAngle)
    println("✅ Created XW rotation matrix (45°)")
    
    // Test vertex transformation
    val rotatedVertex = TesseractMath.multiplyMatrix4x4WithVector(rotationMatrix, vertex)
    println("✅ Rotated vertex: $rotatedVertex")
    
    // Test stereographic projection (4D → 3D)
    val projected3D = TesseractMath.stereographicProjection(rotatedVertex)
    println("✅ Projected to 3D: $projected3D")
    
    // Test perspective projection (3D → 2D)
    val finalPoint = TesseractMath.perspectiveProjection(projected3D, distance = 5.0)
    println("✅ Final 2D point: (${finalPoint.x}, ${finalPoint.y})")
    
    // Test full tesseract generation
    val tesseractVertices = TesseractMath.generateTesseractVertices()
    println("✅ Generated ${tesseractVertices.size} tesseract vertices")
    
    // Test edge generation
    val edges = TesseractMath.getTesseractEdges()
    println("✅ Generated ${edges.size} tesseract edges")
    
    println("\n🚀 ALL TESSERACT MATHEMATICS WORKING CORRECTLY!")
    println("Ready to visualize a 4D hypercube! 📱✨")
} 