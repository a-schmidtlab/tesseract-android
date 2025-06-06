# iOS to Android Port Plan - SofaRotator (Tesseract Visualizer)

## 📱 App Overview

**Original iOS App**: SofaRotator - Interactive 4D Tesseract (Hypercube) Visualizer
- **Primary Function**: Real-time 3D visualization of a 4D tesseract (hypercube) with interactive rotation controls
- **Key Features**: Multiple 4D rotation planes, wireframe/solid rendering modes, educational content, mathematical formulas
- **Target**: Educational tool for understanding higher-dimensional mathematics
- **UI Framework**: SwiftUI (iOS) → Jetpack Compose (Android)

---

## 🎯 Core Features to Port

### 1. **Interactive 4D Tesseract Visualization**
- Real-time rendering of tesseract wireframe and solid modes
- 4D rotation across multiple planes: XW, YZ, XY, ZW
- Perspective and orthographic projection modes
- Depth-based opacity calculations for realistic 3D appearance
- Cross-section visualization with adjustable position

### 2. **Advanced Controls**
- Individual rotation speed controls for each 4D plane
- Auto-rotation with global speed adjustment
- Axis locking capabilities (lock individual rotation planes)
- Visualization mode switching (wireframe ↔ solid)
- Cross-section toggle and position slider

### 3. **Educational Content**
- Comprehensive information view with mathematical explanations
- Interactive dimensional progression visualization (Point → Line → Square → Cube → Tesseract)
- Mathematical formulas with proper rendering
- Historical context and applications

### 4. **Device Integration**
- Motion sensor integration (CoreMotion → Android SensorManager)
- Portrait orientation lock
- High-performance rendering with 60fps target

---

## 🛠️ Technical Architecture Migration

### iOS → Android Technology Mapping

| **iOS Component** | **Android Equivalent** | **Implementation Notes** |
|-------------------|------------------------|--------------------------|
| SwiftUI | Jetpack Compose | Modern declarative UI framework |
| CADisplayLink | Choreographer | Frame-rate synchronized updates |
| CoreMotion | SensorManager | Device motion and orientation |
| Canvas (SwiftUI) | Canvas (Compose) | Custom drawing and graphics |
| Combine | StateFlow/Flow | Reactive programming |
| @State/@Binding | remember/mutableStateOf | State management |
| NavigationStack | Navigation Component | Screen navigation |

---

## 📁 Project Structure

```
SofaRotatorAndroid/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/sofarotator/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ui/
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Typography.kt
│   │   │   │   ├── screens/
│   │   │   │   │   ├── TesseractScreen.kt
│   │   │   │   │   ├── ControlsScreen.kt
│   │   │   │   │   └── InfoScreen.kt
│   │   │   │   ├── components/
│   │   │   │   │   ├── TesseractCanvas.kt
│   │   │   │   │   ├── ControlPanel.kt
│   │   │   │   │   ├── RotationControls.kt
│   │   │   │   │   └── MathFormulas.kt
│   │   │   │   └── utils/
│   │   │   │       ├── TesseractMath.kt
│   │   │   │       ├── MotionManager.kt
│   │   │   │       └── DisplayLinkHelper.kt
│   │   │   └── data/
│   │   │       └── TesseractState.kt
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   └── colors.xml
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── settings.gradle.kts
└── build.gradle.kts
```

---

## 🔧 Implementation Steps

### Phase 1: Project Setup (1-2 days)

#### 1.1 Initialize Android Project
```bash
# Use the Android port guide template provided
mkdir SofaRotatorAndroid && cd SofaRotatorAndroid

# Copy gradle wrapper and basic structure from port guide
# Set up build.gradle.kts with required dependencies
```

#### 1.2 Dependencies Configuration
```kotlin
// app/build.gradle.kts
dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata")
    
    // For sensor access
    implementation("androidx.core:core-ktx:1.12.0")
    
    // For mathematical operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

#### 1.3 Manifest Configuration
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.VIBRATE" />

<activity
    android:name=".MainActivity"
    android:screenOrientation="portrait"
    android:theme="@style/Theme.SofaRotator">
</activity>
```

### Phase 2: Core Mathematics & 3D Engine (3-4 days)

#### 2.1 4D Mathematics Implementation
**File**: `TesseractMath.kt`

```kotlin
object TesseractMath {
    // 4D rotation matrices
    fun rotationMatrixXW(angle: Double): Array<DoubleArray>
    fun rotationMatrixYZ(angle: Double): Array<DoubleArray>
    fun rotationMatrixXY(angle: Double): Array<DoubleArray>
    fun rotationMatrixZW(angle: Double): Array<DoubleArray>
    
    // Vertex generation and transformation
    fun generateTesseractVertices(): List<Vector4D>
    fun applyRotations(vertices: List<Vector4D>, rotations: RotationState): List<Vector4D>
    
    // 4D to 3D projection
    fun projectTo3D(vertex4D: Vector4D, projectionMode: ProjectionMode): Vector3D
    
    // 3D to 2D screen projection
    fun projectToScreen(vertex3D: Vector3D, screenSize: Size, scale: Float): Point
    
    // Depth and opacity calculations
    fun calculateDepth(vertex3D: Vector3D): Double
    fun calculateOpacity(depth: Double): Float
}

data class Vector4D(val x: Double, val y: Double, val z: Double, val w: Double)
data class Vector3D(val x: Double, val y: Double, val z: Double)
data class RotationState(val xw: Double, val yz: Double, val xy: Double, val zw: Double)
```

#### 2.2 State Management
**File**: `TesseractState.kt`

```kotlin
@Stable
data class TesseractViewState(
    // Rotation angles
    val xwRotation: Double = 0.0,
    val yzRotation: Double = 0.0,
    val xyRotation: Double = 0.0,
    val zwRotation: Double = 0.0,
    
    // Rotation speeds
    val xwSpeed: Double = 1.0,
    val yzSpeed: Double = 0.7,
    val xySpeed: Double = 0.5,
    val zwSpeed: Double = 0.3,
    
    // Control states
    val isAutoRotating: Boolean = true,
    val autoRotationSpeed: Double = 1.0,
    val showControls: Boolean = false,
    
    // Visualization settings
    val visualizationMode: VisualizationMode = VisualizationMode.WIREFRAME,
    val projectionMode: ProjectionMode = ProjectionMode.PERSPECTIVE,
    
    // Axis locks
    val lockXW: Boolean = false,
    val lockYZ: Boolean = false,
    val lockXY: Boolean = false,
    val lockZW: Boolean = false,
    
    // Cross-section
    val showCrossSection: Boolean = false,
    val crossSectionPosition: Double = 0.0
)

class TesseractViewModel : ViewModel() {
    private val _state = mutableStateOf(TesseractViewState())
    val state: State<TesseractViewState> = _state
    
    fun updateRotations(deltaTime: Float) { /* Implementation */ }
    fun toggleAutoRotation() { /* Implementation */ }
    fun setVisualizationMode(mode: VisualizationMode) { /* Implementation */ }
    // ... other state updates
}
```

### Phase 3: Custom Canvas Rendering (4-5 days)

#### 3.1 Main Tesseract Canvas
**File**: `TesseractCanvas.kt`

```kotlin
@Composable
fun TesseractCanvas(
    state: TesseractViewState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) { 
        val center = Offset(size.width / 2f, size.height / 2f)
        val scale = minOf(size.width, size.height) * 4.0f
        
        // Generate and transform vertices
        val vertices4D = TesseractMath.generateTesseractVertices()
        val rotations = RotationState(state.xwRotation, state.yzRotation, state.xyRotation, state.zwRotation)
        val rotatedVertices = TesseractMath.applyRotations(vertices4D, rotations)
        
        // Project to 3D then to screen coordinates
        val projectedVertices = rotatedVertices.map { vertex4D ->
            val vertex3D = TesseractMath.projectTo3D(vertex4D, state.projectionMode)
            val screenPoint = TesseractMath.projectToScreen(vertex3D, size, scale)
            val depth = TesseractMath.calculateDepth(vertex3D)
            VertexData(screenPoint, depth)
        }
        
        // Split into inner and outer cube
        val innerCube = projectedVertices.take(8)
        val outerCube = projectedVertices.drop(8)
        
        when (state.visualizationMode) {
            VisualizationMode.WIREFRAME -> drawWireframe(innerCube, outerCube)
            VisualizationMode.SOLID -> drawSolid(innerCube, outerCube)
        }
        
        if (state.showCrossSection) {
            drawCrossSection(state.crossSectionPosition, scale, center)
        }
    }
}

private fun DrawScope.drawWireframe(
    innerCube: List<VertexData>,
    outerCube: List<VertexData>
) {
    // Draw cube edges
    drawCubeEdges(innerCube, Color.Cyan)
    drawCubeEdges(outerCube, Color.Blue)
    
    // Draw connecting lines between corresponding vertices
    for (i in 0 until 8) {
        val opacity = calculateOpacity(innerCube[i].depth, outerCube[i].depth)
        drawLine(
            color = Color.Purple.copy(alpha = opacity),
            start = innerCube[i].point,
            end = outerCube[i].point,
            strokeWidth = 2.dp.toPx()
        )
    }
}

private fun DrawScope.drawSolid(
    innerCube: List<VertexData>,
    outerCube: List<VertexData>
) {
    // Define cube faces (in proper winding order)
    val faces = listOf(
        listOf(0, 1, 2, 3), // Front
        listOf(4, 5, 6, 7), // Back
        listOf(0, 1, 5, 4), // Bottom
        listOf(2, 3, 7, 6), // Top
        listOf(0, 3, 7, 4), // Left
        listOf(1, 2, 6, 5)  // Right
    )
    
    // Draw faces for both cubes
    drawCubeFaces(outerCube, faces, Color.Blue)
    drawCubeFaces(innerCube, faces, Color.Cyan)
    
    // Draw connecting edges
    drawWireframe(innerCube, outerCube)
}
```

#### 3.2 High-Performance Animation
**File**: `DisplayLinkHelper.kt`

```kotlin
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
        choreographer.postFrameCallback(frameCallback)
    }
    
    fun stop() {
        choreographer.removeFrameCallback(frameCallback)
    }
}

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
```

### Phase 4: User Interface (3-4 days)

#### 4.1 Main Screen
**File**: `TesseractScreen.kt`

```kotlin
@Composable
fun TesseractScreen(
    viewModel: TesseractViewModel = viewModel()
) {
    val state by viewModel.state
    
    // Frame callback for animation
    rememberFrameCallback(
        enabled = state.isAutoRotating || !state.isAutoRotating
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
        
        // Top control bar
        TopAppBar(
            title = { Text("SofaRotator") },
            navigationIcon = {
                IconButton(onClick = { viewModel.toggleControls() }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleAutoRotation() }) {
                    Icon(
                        if (state.isAutoRotating) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isAutoRotating) "Pause" else "Play"
                    )
                }
                IconButton(onClick = { /* Navigate to info */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info")
                }
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )
        
        // Controls panel (slide-up)
        AnimatedVisibility(
            visible = state.showControls,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ControlPanel(
                state = state,
                onStateChange = viewModel::updateState
            )
        }
    }
}
```

#### 4.2 Control Panel
**File**: `ControlPanel.kt`

```kotlin
@Composable
fun ControlPanel(
    state: TesseractViewState,
    onStateChange: (TesseractViewState) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Tesseract Controls",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            // Visualization Mode
            item {
                VisualizationModeSelector(
                    currentMode = state.visualizationMode,
                    onModeChange = { mode ->
                        onStateChange(state.copy(visualizationMode = mode))
                    }
                )
            }
            
            // Rotation Controls
            item {
                RotationControlsSection(
                    state = state,
                    onStateChange = onStateChange
                )
            }
            
            // Axis Locks
            item {
                AxisLocksSection(
                    state = state,
                    onStateChange = onStateChange
                )
            }
            
            // Cross-Section
            item {
                CrossSectionControls(
                    showCrossSection = state.showCrossSection,
                    position = state.crossSectionPosition,
                    onToggle = { show ->
                        onStateChange(state.copy(showCrossSection = show))
                    },
                    onPositionChange = { pos ->
                        onStateChange(state.copy(crossSectionPosition = pos))
                    }
                )
            }
        }
    }
}

@Composable
fun RotationControlsSection(
    state: TesseractViewState,
    onStateChange: (TesseractViewState) -> Unit
) {
    Column {
        Text(
            text = "4D Rotation Planes",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // XW Plane
        RotationSlider(
            label = "XW Plane",
            description = "Rotation in X-W dimensional plane",
            value = state.xwSpeed,
            onValueChange = { speed ->
                onStateChange(state.copy(xwSpeed = speed))
            },
            locked = state.lockXW,
            onLockToggle = { locked ->
                onStateChange(state.copy(lockXW = locked))
            }
        )
        
        // YZ Plane
        RotationSlider(
            label = "YZ Plane", 
            description = "Rotation in Y-Z dimensional plane",
            value = state.yzSpeed,
            onValueChange = { speed ->
                onStateChange(state.copy(yzSpeed = speed))
            },
            locked = state.lockYZ,
            onLockToggle = { locked ->
                onStateChange(state.copy(lockYZ = locked))
            }
        )
        
        // Similar for XY and ZW planes...
    }
}

@Composable
fun RotationSlider(
    label: String,
    description: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    locked: Boolean,
    onLockToggle: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (locked) "Locked" else String.format("%.1f×", value),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (locked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconToggleButton(
                    checked = locked,
                    onCheckedChange = onLockToggle
                ) {
                    Icon(
                        if (locked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = if (locked) "Unlock" else "Lock"
                    )
                }
            }
        }
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toDouble()) },
            valueRange = -2f..2f,
            enabled = !locked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### Phase 5: Motion Sensor Integration (1-2 days)

#### 5.1 Motion Manager
**File**: `MotionManager.kt`

```kotlin
class MotionManager(private val context: Context) {
    private val sensorManager: SensorManager = 
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    private val rotationSensor: Sensor? = 
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    
    private var _rotation = MutableStateFlow(0.0)
    val rotation: StateFlow<Double> = _rotation.asStateFlow()
    
    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)
                
                // Convert yaw to degrees
                val yawInDegrees = Math.toDegrees(orientation[0].toDouble())
                _rotation.value = yawInDegrees
            }
        }
        
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes if needed
        }
    }
    
    fun startMonitoring() {
        rotationSensor?.let { sensor ->
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }
    
    fun stopMonitoring() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}

// Usage in Composable
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
```

### Phase 6: Educational Content (2-3 days)

#### 6.1 Information Screen
**File**: `InfoScreen.kt`

```kotlin
@Composable
fun InfoScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Welcome to the 4th Dimension",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Cyan
            )
        }
        
        item {
            IntroductionSection()
        }
        
        item {
            DimensionalAnalysisSection()
        }
        
        item {
            MathematicalFrameworkSection()
        }
        
        item {
            HistoricalSection()
        }
    }
}

@Composable
fun DimensionalAnalysisSection() {
    Column {
        Text(
            text = "Dimensional Analysis",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Cyan
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Let's take a journey through dimensions, starting from the simplest and building our way up to the tesseract.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DimensionalProgressionVisualization()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val dimensionalText = """
            • Point (0D): A single location in space
            • Line (1D): A point extended into length  
            • Square (2D): A line extended into width
            • Cube (3D): A square extended into height
            • Tesseract (4D): A cube extended into the fourth dimension
            
            Each step follows the same pattern: take the previous shape and extend it in a new direction, perpendicular to all previous dimensions.
        """.trimIndent()
        
        Text(
            text = dimensionalText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun DimensionalProgressionVisualization() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Color.Black.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
    ) {
        val centerY = size.height / 2f
        val spacing = size.width / 5f
        
        // Point (0D)
        drawCircle(
            color = Color.Blue,
            radius = 8f,
            center = Offset(spacing * 0.5f, centerY)
        )
        drawCircle(
            color = Color.Cyan,
            radius = 4f,
            center = Offset(spacing * 0.5f, centerY)
        )
        
        // Line (1D)
        drawLine(
            color = Color.Blue,
            start = Offset(spacing * 1.2f, centerY),
            end = Offset(spacing * 1.8f, centerY),
            strokeWidth = 6f
        )
        
        // Square (2D)
        val squareSize = 40f
        val squareCenter = Offset(spacing * 2.5f, centerY)
        drawRect(
            color = Color.Blue,
            topLeft = Offset(
                squareCenter.x - squareSize/2,
                squareCenter.y - squareSize/2
            ),
            size = androidx.compose.ui.geometry.Size(squareSize, squareSize),
            style = Stroke(width = 3f)
        )
        
        // Cube (3D) - with perspective
        val cubeSize = 35f
        val cubeCenter = Offset(spacing * 3.5f, centerY)
        val perspective = 15f
        
        // Front face
        drawRect(
            color = Color.Blue,
            topLeft = Offset(
                cubeCenter.x - cubeSize/2,
                cubeCenter.y - cubeSize/2
            ),
            size = androidx.compose.ui.geometry.Size(cubeSize, cubeSize),
            style = Stroke(width = 3f)
        )
        
        // Back face connections
        val backOffset = Offset(perspective, -perspective)
        drawLine(
            color = Color.Blue.copy(alpha = 0.6f),
            start = Offset(cubeCenter.x - cubeSize/2, cubeCenter.y - cubeSize/2),
            end = Offset(cubeCenter.x - cubeSize/2 + backOffset.x, cubeCenter.y - cubeSize/2 + backOffset.y),
            strokeWidth = 2f
        )
        // Add other cube edges...
        
        // Tesseract representation (simplified)
        val tesseractCenter = Offset(spacing * 4.5f, centerY)
        // Draw simplified tesseract visualization
        drawSimplifiedTesseract(tesseractCenter, 30f)
    }
}

private fun DrawScope.drawSimplifiedTesseract(center: Offset, size: Float) {
    // Draw two overlapping cubes to represent tesseract
    val innerOffset = size * 0.3f
    
    // Outer cube
    drawRect(
        color = Color.Blue,
        topLeft = Offset(center.x - size/2, center.y - size/2),
        size = androidx.compose.ui.geometry.Size(size, size),
        style = Stroke(width = 2f)
    )
    
    // Inner cube
    drawRect(
        color = Color.Cyan,
        topLeft = Offset(center.x - innerOffset/2, center.y - innerOffset/2),
        size = androidx.compose.ui.geometry.Size(innerOffset, innerOffset),
        style = Stroke(width = 2f)
    )
    
    // Connection lines
    val corners = listOf(
        Offset(-size/2, -size/2), Offset(size/2, -size/2),
        Offset(size/2, size/2), Offset(-size/2, size/2)
    )
    val innerCorners = listOf(
        Offset(-innerOffset/2, -innerOffset/2), Offset(innerOffset/2, -innerOffset/2),
        Offset(innerOffset/2, innerOffset/2), Offset(-innerOffset/2, innerOffset/2)
    )
    
    corners.zip(innerCorners).forEach { (outer, inner) ->
        drawLine(
            color = Color.Purple.copy(alpha = 0.7f),
            start = center + outer,
            end = center + inner,
            strokeWidth = 1.5f
        )
    }
}
```

#### 6.2 Mathematical Formulas
**File**: `MathFormulas.kt`

```kotlin
@Composable
fun MathFormulaView(
    type: FormulaType,
    modifier: Modifier = Modifier
) {
    when (type) {
        FormulaType.ROTATION_MATRIX -> RotationMatrixFormula(modifier)
        FormulaType.QUATERNION -> QuaternionFormula(modifier)
        FormulaType.PROJECTION -> ProjectionFormula(modifier)
        FormulaType.DIMENSIONAL_MAPPING -> DimensionalMappingFormula(modifier)
        FormulaType.VERTEX_COUNT -> VertexCountFormula(modifier)
    }
}

@Composable
fun RotationMatrixFormula(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(listOf(Color.Blue.copy(alpha = 0.3f), Color.Cyan.copy(alpha = 0.3f)))
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "4D Rotation Matrix (XW-plane)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Cyan
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Matrix representation using Canvas for precise layout
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.CYAN
                    textSize = 16.sp.toPx()
                    typeface = android.graphics.Typeface.MONOSPACE
                    isAntiAlias = true
                }
                
                val matrix = listOf(
                    "⎛ cos θ    0      0    -sin θ ⎞",
                    "⎜   0      1      0      0    ⎟",
                    "⎜   0      0      1      0    ⎟", 
                    "⎝ sin θ    0      0    cos θ  ⎠"
                )
                
                val lineHeight = size.height / matrix.size
                
                matrix.forEachIndexed { index, row ->
                    drawContext.canvas.nativeCanvas.drawText(
                        row,
                        size.width * 0.1f,
                        lineHeight * (index + 0.7f),
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
fun QuaternionFormula(modifier: Modifier = Modifier) {
    FormulaCard(
        title = "Quaternion Rotation",
        formula = "q = cos(θ/2) + v·sin(θ/2)",
        modifier = modifier
    )
}

@Composable
fun ProjectionFormula(modifier: Modifier = Modifier) {
    FormulaCard(
        title = "4D to 3D Projection",
        formula = "P(x,y,z,w) = (x/(1-w), y/(1-w), z/(1-w))",
        modifier = modifier
    )
}

@Composable
fun FormulaCard(
    title: String,
    formula: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(listOf(Color.Blue.copy(alpha = 0.3f), Color.Cyan.copy(alpha = 0.3f)))
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Cyan
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = formula,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                color = Color.Cyan,
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class FormulaType {
    ROTATION_MATRIX,
    QUATERNION,
    PROJECTION,
    DIMENSIONAL_MAPPING,
    VERTEX_COUNT
}
```

### Phase 7: Navigation & Polish (1-2 days)

#### 7.1 Navigation Setup
**File**: `MainActivity.kt`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Lock orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        setContent {
            SofaRotatorTheme {
                SofaRotatorApp()
            }
        }
    }
}

@Composable
fun SofaRotatorApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "tesseract"
    ) {
        composable("tesseract") {
            TesseractScreen(
                onNavigateToInfo = {
                    navController.navigate("info")
                }
            )
        }
        
        composable("info") {
            InfoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

#### 7.2 Theme Configuration
**File**: `Theme.kt`

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00BCD4), // Cyan
    secondary = Color(0xFF2196F3), // Blue
    tertiary = Color(0xFF9C27B0), // Purple
    background = Color(0xFF000000), // Black
    surface = Color(0xFF111111),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun SofaRotatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
```

### Phase 8: Testing & Optimization (2-3 days)

#### 8.1 Performance Optimization
- **GPU Rendering**: Ensure Canvas operations are hardware-accelerated
- **Frame Rate**: Maintain 60fps target with frame pacing
- **Memory Management**: Proper object pooling for vertices and calculations
- **Battery Optimization**: Reduce computation when app is in background

#### 8.2 Device Testing
- **Screen Sizes**: Test on various Android screen sizes and densities
- **Performance**: Test on low-end devices (API 24+)
- **Sensors**: Verify motion sensor functionality across different devices
- **Orientation**: Ensure portrait lock works correctly

#### 8.3 Edge Cases
- **Sensor Unavailability**: Graceful fallback when motion sensors aren't available
- **Memory Pressure**: Handle low memory conditions
- **Background/Foreground**: Proper pause/resume of animations

---

## 🎨 UI/UX Considerations

### Visual Consistency
- **Color Scheme**: Maintain cyan/blue/purple color palette from iOS version
- **Typography**: Use system fonts with proper hierarchy
- **Animations**: Smooth 60fps animations with proper easing
- **Dark Theme**: Optimized for dark environments (black background)

### Android-Specific Adaptations
- **Material Design 3**: Use Material 3 components while maintaining app identity
- **Navigation**: Bottom sheet controls instead of modal overlays
- **System Integration**: Proper status bar styling, edge-to-edge display
- **Accessibility**: Support for TalkBack and large text sizes

### Performance Targets
- **60fps**: Maintain smooth animation on mid-range devices (2019+)
- **Memory**: Keep peak memory usage under 200MB
- **Battery**: Optimize for minimal battery drain during extended use
- **Startup**: App ready in under 2 seconds on average hardware

---

## 📚 Dependencies & Libraries

### Core Dependencies
```kotlin
// Jetpack Compose
implementation("androidx.compose:compose-bom:2025.06.00")
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.9.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Sensors
implementation("androidx.core:core-ktx:1.12.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### Optional Enhancements
```kotlin
// For advanced mathematical operations
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

// For performance profiling
debugImplementation("androidx.compose.ui:ui-tooling")
debugImplementation("androidx.compose.ui:ui-test-manifest")
```

---

## 🧪 Testing Strategy

### Unit Tests
- **Mathematical Functions**: Verify 4D rotation matrices and projections
- **State Management**: Test ViewModel state transitions
- **Utility Functions**: Depth calculations, opacity functions

### Integration Tests
- **Canvas Rendering**: Verify correct vertex positioning
- **Animation System**: Test frame rate consistency
- **Sensor Integration**: Mock sensor data for testing

### UI Tests
- **Navigation**: Screen transitions work correctly
- **Controls**: All sliders and toggles function properly
- **Accessibility**: TalkBack navigation works

---

## 🚀 Deployment & Distribution

### Build Configuration
```kotlin
android {
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.sofarotator"
        minSdk = 24 // Android 7.0+
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug") // For testing
        }
    }
}
```

### Distribution Options
1. **Google Play Store**: Standard distribution
2. **F-Droid**: Open source app store (if open sourced)
3. **Direct APK**: Side-loading for testing/friends
4. **Internal Testing**: Play Console internal testing track

---

## ⏱️ Development Timeline

**REALISTIC TIMELINE: 2-3 days** *(since we're translating existing code, not building from scratch)*

### **Day 1: Core Port (6-8 hours)**
| **Phase** | **Duration** | **Key Deliverables** |
|-----------|-------------|---------------------|
| **Project Setup** | 30 mins | Android project structure, dependencies, gradle config |
| **Math Engine Translation** | 2-3 hours | Swift→Kotlin conversion of 4D mathematics, rotation matrices |
| **Canvas Rendering** | 2-3 hours | SwiftUI Canvas → Compose Canvas, wireframe/solid modes |
| **Basic UI Structure** | 1-2 hours | Main screen layout, navigation framework |

### **Day 2: Features & Polish (6-8 hours)**
| **Phase** | **Duration** | **Key Deliverables** |
|-----------|-------------|---------------------|
| **Control Panels** | 2-3 hours | Sliders, toggles, settings panel, rotation controls |
| **Motion Sensors** | 1 hour | CoreMotion → SensorManager integration |
| **Educational Content** | 2-3 hours | Info screens, mathematical formulas, explanations |
| **Navigation & Theming** | 1 hour | Screen transitions, Material Design 3 theming |

### **Day 3: Testing & Refinement (Optional, 2-4 hours)**
| **Phase** | **Duration** | **Key Deliverables** |
|-----------|-------------|---------------------|
| **Device Testing** | 1-2 hours | Test on various Android devices, performance optimization |
| **Final Polish** | 1-2 hours | Edge cases, final touches, APK generation |

**Total Estimated Duration: 2-3 days** *(14-20 hours with AI assistance)*

> **Why So Fast?** 
> - ✅ **All mathematics already solved** - Direct Swift→Kotlin translation
> - ✅ **UI/UX already designed** - No design decisions needed
> - ✅ **Features clearly defined** - Exact specification exists
> - ✅ **AI-assisted development** - Rapid code generation and translation
> - ✅ **Similar frameworks** - SwiftUI and Jetpack Compose are very comparable

---

## 🎯 Success Criteria

### Functional Requirements
- ✅ Interactive 4D tesseract visualization with smooth 60fps animation
- ✅ Multiple rotation planes (XW, YZ, XY, ZW) with individual speed controls
- ✅ Wireframe and solid rendering modes
- ✅ Cross-section visualization
- ✅ Motion sensor integration for device-based rotation
- ✅ Comprehensive educational content with mathematical explanations
- ✅ Intuitive touch controls and settings panel

### Performance Requirements
- ✅ Smooth 60fps on devices from 2019+
- ✅ Responsive touch controls with minimal latency
- ✅ Memory usage under 200MB peak
- ✅ App startup under 2 seconds
- ✅ Battery optimized (minimal drain during extended use)

### Quality Requirements
- ✅ Crash-free experience on target devices
- ✅ Accessible to users with disabilities
- ✅ Consistent visual design following Material Design principles
- ✅ Mathematical accuracy in all calculations and projections
- ✅ Educational content that effectively explains 4D concepts

---

## 📝 Notes & Considerations

### Technical Challenges
1. **4D Mathematics**: Implementing accurate 4D rotation matrices and projections
2. **Performance**: Maintaining 60fps with complex mathematical calculations
3. **Canvas Rendering**: Efficient drawing of dynamic geometry
4. **State Management**: Complex state with multiple interdependent variables

### Future Enhancements
1. **VR Support**: ARCore integration for immersive 4D visualization
2. **Interactive Tutorials**: Step-by-step guided exploration
3. **Export Features**: Save animations as videos or images
4. **Advanced Mathematics**: Additional projection modes, section analysis
5. **Customization**: User-defined color schemes, rotation patterns

### Platform Differences
- **iOS Core Motion → Android SensorManager**: Different APIs but similar functionality
- **SwiftUI Canvas → Compose Canvas**: Very similar drawing capabilities
- **CADisplayLink → Choreographer**: Both provide frame-synchronized updates
- **Navigation**: Android Navigation Component vs SwiftUI NavigationStack

This comprehensive port plan maintains the mathematical accuracy and educational value of the original iOS app while leveraging Android's strengths and following modern Android development best practices. 