# Tesseract Android

A 4D Tesseract visualization app for Android built with Kotlin and Jetpack Compose.

## Features

- **4D Tesseract Visualization**: Interactive visualization of a 4-dimensional hypercube (tesseract)
- **Multiple Rotation Planes**: Control rotations in XW, YZ, XY, and ZW planes independently
- **Projection Modes**: Switch between perspective and orthographic projections
- **Rendering Modes**: Choose between wireframe and solid rendering
- **Motion Sensor Integration**: Use device accelerometer for intuitive rotation control
- **Cross-Section View**: Visualize 3D cross-sections of the 4D object

## Technical Implementation

The app implements 4D mathematics based on the original Swift TesseractView:

- **4D Rotations**: Applies rotations in each 4D plane using 2D rotation matrices
- **Stereographic Projection**: Projects 4D coordinates to 3D space
- **Perspective/Orthographic Projection**: Final projection from 3D to 2D screen coordinates
- **Inner/Outer Cube Structure**: Creates two 3D cubes at different w-coordinates to represent the tesseract

## Architecture

- **UI Layer**: Jetpack Compose UI with Material Design 3
- **Math Layer**: Pure Kotlin implementation of 4D mathematics (`TesseractMath.kt`)
- **Canvas Rendering**: Custom Canvas-based 3D wireframe rendering (`TesseractCanvas.kt`)
- **State Management**: Compose state management for rotations and settings

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.8+

### Building

1. Clone the repository:
   ```bash
   git clone https://github.com/a-schmidtlab/tesseract-android.git
   cd tesseract-android
   ```

2. Open in Android Studio

3. Build and run:
   ```bash
   ./gradlew installDebug
   ```

### Running on Emulator

The app has been tested with:
- Pixel 7 API 35 (Baklava) emulator
- Software rendering (`-gpu swiftshader_indirect`)
- Increased memory and partition size for optimal performance

## Project Structure

```
app/src/main/java/com/example/sofarotator/
├── data/
│   └── TesseractMath.kt          # 4D mathematics and projections
├── ui/
│   ├── components/
│   │   ├── TesseractCanvas.kt    # Canvas-based 3D rendering
│   │   └── RotationControls.kt   # UI controls for rotations
│   ├── screens/
│   │   ├── TesseractScreen.kt    # Main visualization screen
│   │   └── InfoScreen.kt         # Information and help screen
│   └── theme/                    # Material Design 3 theming
└── MainActivity.kt               # Main application entry point
```

## Mathematics

The tesseract is represented as two 3D cubes:
- **Inner Cube**: 8 vertices at w = 0.5, scale = 0.5
- **Outer Cube**: 8 vertices at w = -0.5, scale = 1.0

4D rotations are applied using separate 2D rotations in each plane:
- XW plane rotation
- YZ plane rotation  
- XY plane rotation
- ZW plane rotation

Projection pipeline: 4D → 3D (stereographic) → 2D (perspective/orthographic)

## License

This project is open source. Feel free to use and modify according to your needs.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests. 