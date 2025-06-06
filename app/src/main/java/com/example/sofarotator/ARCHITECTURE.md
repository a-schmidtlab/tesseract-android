# 🏗️ SofaRotator Architecture

This document explains the clean, organized architecture of the SofaRotator app.

## 📁 Folder Structure

```
📁 com.example.sofarotator/
├── 📁 domain/              # Core business logic (no UI dependencies)
│   ├── 📁 math/            # 4D mathematics & calculations
│   ├── 📁 models/          # Core data models & enums
│   └── 📁 use_cases/       # Business logic operations (future)
│
├── 📁 presentation/        # UI & state management
│   ├── 📁 state/           # ViewModels & UI state
│   ├── 📁 screens/         # Full screen Composables
│   ├── 📁 components/      # Reusable UI components
│   └── 📁 theme/           # UI theming & styling
│
├── 📁 utils/              # Shared utilities
│   ├── 📁 motion/          # Motion & input handling
│   └── 📁 extensions/      # Kotlin extensions (future)
│
├── MainActivity.kt        # App entry point
└── SofaRotatorApp.kt     # Main app navigation
```

## 🧩 Layer Details

### 🔢 Domain Layer (`domain/`)
**Purpose**: Pure business logic with no Android/UI dependencies

- **`math/`**: Core 4D mathematics
  - `TesseractMath.kt` - 4D rotation, projection, tesseract creation
  - Contains: `Point4D`, `ScreenPoint`, `Rotation4D`, `Tesseract`, `ProjectionMode`

- **`models/`**: Core data models
  - `VisualizationMode.kt` - How to display the tesseract

- **`use_cases/`**: Business operations (planned)
  - Future: Complex tesseract operations, animation logic

### 🎨 Presentation Layer (`presentation/`)
**Purpose**: UI components and state management

- **`state/`**: State management
  - `TesseractState.kt` - UI state, ViewModel, user interactions

- **`screens/`**: Full screen composables
  - `TesseractScreen.kt` - Main tesseract visualization screen
  - `InfoScreen.kt` - Information/help screen

- **`components/`**: Reusable UI components
  - `TesseractCanvas.kt` - 4D tesseract rendering
  - `RotationControls.kt` - User interface controls

- **`theme/`**: UI styling
  - `Theme.kt` - App colors and theme
  - `Typography.kt` - Text styling

### 🛠️ Utils Layer (`utils/`)
**Purpose**: Shared utilities and helpers

- **`motion/`**: Motion and input handling
  - `MotionManager.kt` - Device motion detection
  - `FrameCallback.kt` - Animation frame management

## 🔄 Data Flow

```
User Input → Presentation → Domain → Math Calculations → Back to UI
```

1. **User interacts** with UI components
2. **Presentation layer** updates state
3. **Domain layer** performs 4D calculations
4. **Results flow back** to UI for rendering

## ✅ Benefits of This Structure

### 🎯 **Clear Separation of Concerns**
- Math logic separated from UI
- Easy to test each layer independently
- No circular dependencies

### 🔄 **Easy to Maintain**
- Each file has a single responsibility
- Changes in one layer don't break others
- Clear import paths show dependencies

### 📈 **Scalable**
- Easy to add new features
- Can add new screens/components easily
- Domain logic can be reused

### 🧪 **Testable**
- Domain layer can be unit tested easily
- UI components can be tested separately
- Mock dependencies are straightforward

## 🚀 Adding New Features

### Adding a New UI Component:
```kotlin
// 1. Create in presentation/components/
package com.example.sofarotator.presentation.components

// 2. Import from other layers as needed
import com.example.sofarotator.domain.math.TesseractMath
import com.example.sofarotator.presentation.state.TesseractViewState
```

### Adding New Math Operations:
```kotlin
// 1. Create in domain/math/
package com.example.sofarotator.domain.math

// 2. Keep it pure - no UI dependencies
object NewMathOperations {
    fun complexCalculation(): Result { ... }
}
```

### Adding New Models:
```kotlin
// 1. Create in domain/models/
package com.example.sofarotator.domain.models

data class NewDataModel(...)
```

## 📋 Import Guidelines

- **Domain → Domain**: ✅ Allowed
- **Presentation → Domain**: ✅ Allowed  
- **Utils → Domain**: ✅ Allowed
- **Domain → Presentation**: ❌ **Never!**
- **Domain → Utils**: ❌ **Never!** 

This ensures clean architecture with no circular dependencies. 