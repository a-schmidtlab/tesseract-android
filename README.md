# SofaRotator - 4D Tesseract Visualization


A  4D Tesseract (hypercube) visualization app for Android, built with Kotlin and Jetpack Compose. Watch mathematics come alive as you explore the fourth dimension through your device screen.

## Features

- **Interactive 4D Visualization**: Real-time tesseract rendering with smooth animations
- **Multi-Plane Rotations**: Independent control of XW, YZ, XY, and ZW rotation planes
- **Dual Projection Modes**: Switch between perspective and orthographic projections
- **Rendering Styles**: Choose wireframe or solid rendering with transparency effects
- **Motion Sensor Integration**: Tilt your device to control rotations intuitively
- **Cross-Section Views**: Slice through the 4D object to see 3D cross-sections
- **Smooth Performance**: Optimized rendering with 60 FPS animations

## Architecture Overview

This app follows clean architecture principles with clear separation of concerns:

```
com.example.sofarotator/
├── domain/              # Pure business logic (no Android dependencies)
│   ├── math/            # 4D mathematics & calculations  
│   └── models/          # Core data models & enums
│
├── presentation/        # UI & state management
│   ├── state/           # ViewModels & UI state
│   ├── screens/         # Full screen Composables
│   ├── components/      # Reusable UI components
│   └── theme/           # UI theming & styling
│
├── utils/               # Shared utilities
│   └── motion/          # Motion & input handling
│
├── MainActivity.kt      # App entry point
└── SofaRotatorApp.kt   # Main app navigation
```

## How It Works

### The Mathematicss

The tesseract lives in 4D space, but our screens are 2D. Here's how we bridge that gap:

#### 1. Creating the 4D Structure
```kotlin
// Two 3D cubes at different w-coordinates form our tesseract
val innerCube = createCube(scale = 0.5, wPosition = 0.5)   // Smaller, forward
val outerCube = createCube(scale = 1.0, wPosition = -0.5)  // Larger, backward
```

#### 2. 4D Rotations 
We rotate in four separate 2D planes:
- **XW plane**: Rotating around the Y-Z axis in 4D space
- **YZ plane**: Classic 3D rotation
- **XY plane**: Another classic 3D rotation  
- **ZW plane**: The mysterious fourth dimension rotation

```kotlin
// Each rotation is a simple 2D rotation in its plane
fun rotate2D(x: Double, y: Double, angle: Double): Pair<Double, Double> {
    val cos = cos(angle)
    val sin = sin(angle)
    return Pair(x * cos - y * sin, x * sin + y * cos)
}
```

#### 3. The Projection Pipeline
```
4D Space → Stereographic Projection → 3D Space → Perspective/Orthographic → 2D Screen
```

**Step 1**: 4D → 3D (Stereographic projection)
```kotlin
val distance4D = 5.0
val w1 = 1.0 / (distance4D - point.w)
val x3D = point.x * w1
val y3D = point.y * w1
val z3D = point.z * w1
```

**Step 2**: 3D → 2D (Perspective or orthographic)
```kotlin
when (projectionMode) {
    PERSPECTIVE -> {
        val perspective = 1.0 / (distance3D - z3D)
        Triple(x3D * perspective, y3D * perspective, perspective)
    }
    ORTHOGRAPHIC -> {
        Triple(x3D * 0.5, y3D * 0.5, 1.0 / (distance3D - z3D))
    }
}
```

### The Rendering Process

#### Wireframe Mode
1. Draw connections between corresponding vertices of inner/outer cubes
2. Draw outer cube edges (blue)
3. Draw inner cube edges (cyan) - appears in front

#### Solid Mode  
1. Draw outer cube faces with transparency
2. Draw inner cube faces  
3. Add subtle connection lines for depth perception

### User Interaction Flow

```
User Input → ViewModel → Domain Logic → Math Calculations → Canvas Rendering
```

1. **User touches** slider or tilts device
2. **ViewModel** updates rotation state
3. **Domain layer** calculates new 4D positions
4. **Presentation layer** renders to Canvas

## Key Files Explained

### Domain Layer
- **`TesseractMath.kt`** - The heart of our 4D engine. Contains all mathematical operations for creating, rotating, and projecting the tesseract. *This is where the magic happens!*
- **`VisualizationMode.kt`** - Simple enum defining wireframe vs solid rendering

### Presentation Layer
- **`TesseractState.kt`** - ViewModel managing all UI state, rotation speeds, and user preferences
- **`TesseractCanvas.kt`** - Custom Canvas composable that handles all the drawing operations
- **`RotationControls.kt`** - UI controls for adjusting rotations, speeds, and settings
- **`TesseractScreen.kt`** - Main screen combining canvas and controls
- **`InfoScreen.kt`** - Help screen explaining the app *(because 4D can be confusing)*

### Utils Layer
- **`MotionManager.kt`** - Handles device accelerometer for motion-based rotation
- **`FrameCallback.kt`** - Manages smooth 60 FPS animation updates

## Data Flow Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    User     │───▶│Presentation │───▶│   Domain    │
│  Interaction│    │   Layer     │    │   Layer     │
└─────────────┘    └─────────────┘    └─────────────┘
                           │                   │
                           ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Screen    │◀───│  UI State   │◀───│    Math     │
│  Rendering  │    │ Management  │    │Calculations │
└─────────────┘    └─────────────┘    └─────────────┘
```

## Getting Started

### Prerequisites
- Android Studio Dolphin or later
- Android SDK 24+ (Android 7.0)
- A device or emulator *(preferably one that exists in 3D space)*

### Quick Setup

1. **Clone the repository**:
   ```bash
   git clone <your-repo-url>
   cd SofaRotator
   ```

2. **Open in Android Studio** and let it sync

3. **Run the app**:
   ```bash
   ./gradlew installDebug
   ```

### Emulator Setup
Tested with Pixel 7 API 35 (Baklava):
```bash
# Launch with software rendering for best compatibility
emulator -avd Pixel_7_API_Baklava -gpu swiftshader_indirect -memory 4096 -partition-size 8192
```

## Usage

1. **Auto-Rotation**: Toggle to watch the tesseract rotate automatically
2. **Manual Control**: Use sliders to control each rotation plane
3. **Projection Mode**: Switch between perspective (realistic) and orthographic (technical)
4. **Rendering Style**: Toggle between wireframe and solid modes
5. **Motion Control**: Enable device tilt for immersive control
6. **Cross-Sections**: Visualize 3D slices through the 4D object

## The Science Behind It

A tesseract is to a cube what a cube is to a square. Just as a cube has:
- 8 vertices, 12 edges, 6 faces
- A tesseract has 16 vertices, 32 edges, 24 faces, 8 cubes

We represent this by connecting two 3D cubes displaced along the fourth dimension (w-axis).

**Fun fact**: If you could actually see in 4D, this app would look laughably primitive. Fortunately, we're all stuck in 3D space together.

## Code Structure Benefits

### Clean Architecture
- **Domain layer** contains pure business logic with no Android dependencies
- **Presentation layer** handles UI and state management
- **Utils layer** provides shared functionality

### Maintainability
- Each file has a single, clear responsibility
- Dependencies flow in one direction (no circular references)
- Easy to test each layer independently

### Scalability
- Adding new features follows clear patterns
- Math logic can be reused for other visualizations
- UI components are composable and reusable

## Technical Implementation Details

### Performance Optimizations
- Efficient 4D to 2D projection pipeline
- Optimized Canvas drawing with minimal allocations
- Smooth 60 FPS animations using Choreographer
- Depth-based opacity calculations for realistic rendering

### Mathematical Approach
- Uses the same core algorithms as my original Swift implementation
- Separates rotation calculations into individual 2D plane rotations
- Implements stereographic projection for 4D to 3D conversion
- Supports both perspective and orthographic final projections

## Troubleshooting

- **App crashes on launch**: Check if your device/emulator supports OpenGL ES 2.0
- **Jerky animations**: Try reducing auto-rotation speed or enable software rendering
- **Black screen**: Ensure the app has proper permissions and the emulator is fully booted

## Contributing

Found a bug? Want to add a 5D hypercube? *(Please don't)* 

Contributions welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes following the existing architecture
4. Submit a pull request

## License

This project is open source. Feel free to use it to impress your friends with your superior understanding of higher-dimensional mathematics.

## Acknowledgments

- Inspired by the my Swift TesseractView implementation
- Thanks to the Jetpack Compose team for making 2D rendering enjoyable
- Special thanks to all the mathematicians who figured out 4D space so we don't have to

---

*"The fourth dimension is like explaining color to someone who's only ever seen in black and white. This app probably won't help with that, but at least it looks cool."* 

(c) 2025 by Axel Schmidt