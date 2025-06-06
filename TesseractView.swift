/*
 * TesseractView.swift
 * SofaRotator
 *
 * Created by Axel Schmidt on 10.02.25.
 * Copyright © 2025 Axel Schmidt. All rights reserved.
 */

import SwiftUI
import Combine
import QuartzCore

/// Visualization mode for the tesseract
enum TesseractVisualizationMode {
    case wireframe
    case solid
}

/// Projection mode for the tesseract
enum TesseractProjectionMode {
    case perspective
    case orthographic
}

/// Main view for tesseract visualization
struct TesseractView: View {
    // Color scheme
    private let primaryColor = Color.cyan
    private let secondaryColor = Color.blue
    private let accentColor = Color.purple
    private let backgroundColor = Color.black
    
    // State variables for menu
    @State private var showControls = false
    
    // Rotation angles for different 4D planes
    @State private var xwRotation: Double = 0
    @State private var yzRotation: Double = 0
    @State private var xyRotation: Double = 0
    @State private var zwRotation: Double = 0
    
    // Rotation speeds for different planes (optimized for real device)
    @State private var xwSpeed: Double = 1.0
    @State private var yzSpeed: Double = 0.7
    @State private var xySpeed: Double = 0.5
    @State private var zwSpeed: Double = 0.3
    
    // Slider positions for individual speed control
    @State private var xwSliderPosition: Double = 0.5
    @State private var yzSliderPosition: Double = 0.35
    @State private var xySliderPosition: Double = 0.25
    @State private var zwSliderPosition: Double = 0.15
    
    // Auto-rotation
    @State private var isAutoRotating: Bool = true
    @State private var autoRotationSpeed: Double = 1.0
    
    // Visualization settings
    @State private var visualizationMode: TesseractVisualizationMode = .wireframe
    @State private var projectionMode: TesseractProjectionMode = .perspective
    
    // Axis locks
    @State private var lockXW: Bool = false
    @State private var lockYZ: Bool = false
    @State private var lockXY: Bool = false
    @State private var lockZW: Bool = false
    
    // Cross-section settings
    @State private var showCrossSection: Bool = false
    @State private var crossSectionPosition: Double = 0
    
    // Replace timer with DisplayLink
    @State private var displayLink: CADisplayLink?
    @State private var lastUpdateTime: CFTimeInterval = 0
    
    // Keep a strong reference to the target
    @State private var displayLinkTarget: DisplayLinkTarget?
    
    private func setupDisplayLink() {
        displayLink?.invalidate()
        let target = DisplayLinkTarget(update: updateRotations)
        displayLink = CADisplayLink(
            target: target,
            selector: #selector(DisplayLinkTarget.handleUpdate)
        )
        displayLink?.preferredFramesPerSecond = 60 // Set to 60fps for better performance
        displayLink?.add(to: .main, forMode: .common)
    }
    
    private func updateRotations() {
        let currentTime = CACurrentMediaTime()
        let delta = min(1.0/30.0, currentTime - lastUpdateTime) // Cap delta time
        lastUpdateTime = currentTime
        
        let rotationDelta = 0.02 * delta * 60 // Scale by delta time and target 60fps
        
        if isAutoRotating {
            if !lockXW { xwRotation += rotationDelta * xwSpeed * autoRotationSpeed }
            if !lockYZ { yzRotation += rotationDelta * yzSpeed * autoRotationSpeed }
            if !lockXY { xyRotation += rotationDelta * xySpeed * autoRotationSpeed }
            if !lockZW { zwRotation += rotationDelta * zwSpeed * autoRotationSpeed }
        } else {
            xwRotation += rotationDelta * xwSliderPosition * 2
            yzRotation += rotationDelta * yzSliderPosition * 2
            xyRotation += rotationDelta * xySliderPosition * 2
            zwRotation += rotationDelta * zwSliderPosition * 2
        }
    }
    
    init(preview: Bool = false) {
        if preview {
            // Set static values for preview
            _xwRotation = State(initialValue: 0.5)
            _yzRotation = State(initialValue: 0.3)
            _xyRotation = State(initialValue: 0.2)
            _zwRotation = State(initialValue: 0.1)
            _isAutoRotating = State(initialValue: false)
        }
    }
    
    var body: some View {
        NavigationStack {
            GeometryReader { geometry in
                ZStack {
                    // Background
                    Color.black
                        .ignoresSafeArea()
                    
                    // Initialize slider positions and start auto-rotation when view appears
                    .onAppear {
                        xwSliderPosition = 0.5
                        yzSliderPosition = 0.35
                        xySliderPosition = 0.25
                        zwSliderPosition = 0.15
                        isAutoRotating = true
                        autoRotationSpeed = 1.0
                        lastUpdateTime = CACurrentMediaTime()
                        
                        // Create and store the target
                        displayLinkTarget = DisplayLinkTarget(update: updateRotations)
                        displayLink = CADisplayLink(
                            target: displayLinkTarget!,
                            selector: #selector(DisplayLinkTarget.handleUpdate)
                        )
                        displayLink?.preferredFramesPerSecond = 60
                        displayLink?.add(to: .main, forMode: .common)
                    }
                    
                    // Main visualization
                    Canvas { context, size in
                        let center = CGPoint(x: size.width/2, y: size.height/2)
                        let scale = min(size.width, size.height) * 4.0
                        
                        // Create vertices with enhanced 4D perspective
                        let innerCube = createVertices(
                            scale: 0.5,
                            w: 0.5,
                            angles: (
                                xw: lockXW ? 0 : xwRotation,
                                yz: lockYZ ? 0 : yzRotation,
                                xy: lockXY ? 0 : xyRotation,
                                zw: lockZW ? 0 : zwRotation
                            ),
                            viewScale: scale,
                            center: center
                        )
                        
                        let outerCube = createVertices(
                            scale: 1.0,
                            w: -0.5,
                            angles: (
                                xw: lockXW ? 0 : xwRotation,
                                yz: lockYZ ? 0 : yzRotation,
                                xy: lockXY ? 0 : xyRotation,
                                zw: lockZW ? 0 : zwRotation
                            ),
                            viewScale: scale,
                            center: center
                        )
                        
                        switch visualizationMode {
                        case .wireframe:
                            drawWireframe(context: context, innerCube: innerCube, outerCube: outerCube)
                        case .solid:
                            drawSolid(context: context, innerCube: innerCube, outerCube: outerCube)
                        }
                        
                        if showCrossSection {
                            drawCrossSection(context: context, position: crossSectionPosition, scale: scale, center: center)
                        }
                    }
                    .ignoresSafeArea()
                    
                    // Top Bar with Buttons
                    VStack {
                        HStack {
                            // Settings Button
                            Button(action: { showControls.toggle() }) {
                                Image(systemName: "slider.horizontal.3")
                                    .font(.title2)
                                    .foregroundColor(primaryColor)
                                    .frame(width: 44, height: 44)
                                    .background(backgroundColor.opacity(0.6))
                                    .clipShape(Circle())
                            }
                            
                            Spacer()
                            
                            // Info Button
                            NavigationLink(destination: TesseractInfoView()) {
                                Image(systemName: "info.circle")
                                    .font(.title2)
                                    .foregroundColor(primaryColor)
                                    .frame(width: 44, height: 44)
                                    .background(backgroundColor.opacity(0.6))
                                    .clipShape(Circle())
                            }
                        }
                        .padding(.horizontal)
                        .padding(.top, 8)
                        
                        Spacer()
                    }
                }
            }
            .navigationBarHidden(true)
        }
        .sheet(isPresented: $showControls) {
            TesseractControlsView(
                visualizationMode: $visualizationMode,
                projectionMode: $projectionMode,
                isAutoRotating: $isAutoRotating,
                autoRotationSpeed: $autoRotationSpeed,
                xwRotation: $xwSliderPosition,
                yzRotation: $yzSliderPosition,
                xyRotation: $xySliderPosition,
                zwRotation: $zwSliderPosition,
                lockXW: $lockXW,
                lockYZ: $lockYZ,
                lockXY: $lockXY,
                lockZW: $lockZW,
                showCrossSection: $showCrossSection,
                crossSectionPosition: $crossSectionPosition,
                primaryColor: primaryColor,
                secondaryColor: secondaryColor,
                accentColor: accentColor
            )
            .presentationDetents([.large])
            .presentationDragIndicator(.visible)
        }
        .onDisappear {
            displayLink?.invalidate()
            displayLink = nil
            displayLinkTarget = nil
        }
    }
    
    /// Erzeugt die Eckpunkte für einen Würfel im 4D-Raum
    /// - Parameters:
    ///   - scale: Skalierungsfaktor für die Größe
    ///   - w: Position in der vierten Dimension
    ///   - angle: Rotationswinkel
    ///   - viewScale: Ansichtsskalierung
    ///   - center: Bildschirmmittelpunkt
    private func createVertices(scale: Double, w: Double, angles: (xw: Double, yz: Double, xy: Double, zw: Double), viewScale: Double, center: CGPoint) -> [(point: CGPoint, depth: Double)] {
        // Würfeleckpunkte im 3D-Raum
        let vertices = [
            (-scale, -scale, -scale), (scale, -scale, -scale),
            (scale, scale, -scale), (-scale, scale, -scale),
            (-scale, -scale, scale), (scale, -scale, scale),
            (scale, scale, scale), (-scale, scale, scale)
        ]
        
        return vertices.map { (x, y, z) in
            let projected = project4D(
                x: x, y: y, z: z, w: w,
                xwAngle: angles.xw,
                yzAngle: angles.yz,
                xyAngle: angles.xy,
                zwAngle: angles.zw,
                scale: viewScale,
                center: center
            )
            return (point: projected.point, depth: projected.depth)
        }
    }
    
    /// Projektionsberechnung für 4D → 2D Transformation
    private func project4D(
        x: Double, y: Double, z: Double, w: Double,
        xwAngle: Double, yzAngle: Double, xyAngle: Double, zwAngle: Double,
        scale: Double, center: CGPoint
    ) -> (point: CGPoint, depth: Double) {
        // Rotationen in verschiedenen 4D-Ebenen (Drehungen)
        var (rotX, rotW) = rotate2D(x: x, y: w, angle: xwAngle)
        var (rotY, rotZ) = rotate2D(x: y, y: z, angle: yzAngle)
        (rotX, rotY) = rotate2D(x: rotX, y: rotY, angle: xyAngle)
        (rotZ, rotW) = rotate2D(x: rotZ, y: rotW, angle: zwAngle)
        
        // Stereographische Projektion: 4D → 3D (Raumprojektion)
        let distance = 5.0
        let w1 = 1.0 / (distance - rotW)
        let projX = rotX * w1
        let projY = rotY * w1
        let projZ = rotZ * w1
        
        // View distance for both projection modes
        let viewDistance = 4.0
        var screenX: Double
        var screenY: Double
        var depth: Double
        
        switch projectionMode {
        case .perspective:
            // Perspektivische Projektion: 3D → 2D mit Tiefeneffekt
            let perspective = 1.0 / (viewDistance - projZ)
            screenX = projX * perspective
            screenY = projY * perspective
            depth = perspective
            
        case .orthographic:
            // Orthographische Projektion: 3D → 2D ohne Tiefenverzerrung
            screenX = projX * 0.5 // Scale factor for orthographic view
            screenY = projY * 0.5
            depth = 1.0 / (viewDistance - projZ) // Keep depth for shading
        }
        
        return (
            point: CGPoint(
                x: center.x + screenX * scale,
                y: center.y + screenY * scale
            ),
            depth: depth
        )
    }
    
    /// Rotationsmatrix für 2D-Ebene (Drehmatrix)
    private func rotate2D(x: Double, y: Double, angle: Double) -> (Double, Double) {
        let cosA = cos(angle)
        let sinA = sin(angle)
        return (
            x * cosA - y * sinA,
            x * sinA + y * cosA
        )
    }
    
    /// Zeichnet Verbindungslinien zwischen inneren und äußeren Würfelpunkten
    private func drawConnections(context: GraphicsContext, from: [(point: CGPoint, depth: Double)], to: [(point: CGPoint, depth: Double)]) {
        for i in 0..<8 {
            let path = Path { p in
                p.move(to: from[i].point)
                p.addLine(to: to[i].point)
            }
            
            // Berechne Transparenz basierend auf Tiefe
            let avgDepth = (from[i].depth + to[i].depth) / 2
            let opacity = calculateOpacity(depth: avgDepth)
            
            // Farbverlauf basierend auf Tiefe
            let gradient = Gradient(colors: [
                Color.blue.opacity(opacity * 0.8),
                Color.cyan.opacity(opacity * 0.6)
            ])
            
            context.stroke(
                path,
                with: .linearGradient(
                    gradient,
                    startPoint: from[i].point,
                    endPoint: to[i].point
                ),
                lineWidth: 2.0
            )
        }
    }
    
    /// Zeichnet einen Würfel mit tiefenbasierter Transparenz
    private func drawCube(context: GraphicsContext, vertices: [(point: CGPoint, depth: Double)]) {
        let edges = [
            (0, 1), (1, 2), (2, 3), (3, 0),  // Vorderseite
            (4, 5), (5, 6), (6, 7), (7, 4),  // Rückseite
            (0, 4), (1, 5), (2, 6), (3, 7)   // Verbindungskanten
        ]
        
        for edge in edges {
            let path = Path { p in
                p.move(to: vertices[edge.0].point)
                p.addLine(to: vertices[edge.1].point)
            }
            
            let avgDepth = (vertices[edge.0].depth + vertices[edge.1].depth) / 2
            let opacity = calculateOpacity(depth: avgDepth)
            
            let gradient = Gradient(colors: [
                Color.blue.opacity(opacity),
                Color.cyan.opacity(opacity * 0.8)
            ])
            
            context.stroke(
                path,
                with: .linearGradient(
                    gradient,
                    startPoint: vertices[edge.0].point,
                    endPoint: vertices[edge.1].point
                ),
                lineWidth: 2.0
            )
        }
    }
    
    /// Berechnet die Transparenz basierend auf der Tiefe
    private func calculateOpacity(depth: Double) -> Double {
        return min(max(0.2 + depth * 2, 0.2), 1.0)
    }
    
    /// Draws the tesseract in wireframe mode
    private func drawWireframe(context: GraphicsContext, innerCube: [(point: CGPoint, depth: Double)], outerCube: [(point: CGPoint, depth: Double)]) {
        drawConnections(context: context, from: innerCube, to: outerCube)
        drawCube(context: context, vertices: outerCube)
        drawCube(context: context, vertices: innerCube)
    }
    
    /// Draws the tesseract in solid mode with faces
    private func drawSolid(context: GraphicsContext, innerCube: [(point: CGPoint, depth: Double)], outerCube: [(point: CGPoint, depth: Double)]) {
        // Define faces for each cube (clockwise winding)
        let faces = [
            [0, 1, 2, 3], // Front
            [4, 5, 6, 7], // Back
            [0, 1, 5, 4], // Bottom
            [2, 3, 7, 6], // Top
            [0, 3, 7, 4], // Left
            [1, 2, 6, 5]  // Right
        ]
        
        // Draw outer cube faces
        for face in faces {
            let path = Path { p in
                p.move(to: outerCube[face[0]].point)
                for i in 1...3 {
                    p.addLine(to: outerCube[face[i]].point)
                }
                p.closeSubpath()
            }
            
            // Calculate average depth for face
            let avgDepth = face.reduce(0.0) { $0 + outerCube[$1].depth } / 4.0
            let opacity = calculateOpacity(depth: avgDepth)
            
            // Fill face with gradient
            let gradient = Gradient(colors: [
                Color.blue.opacity(opacity * 0.3),
                Color.cyan.opacity(opacity * 0.2)
            ])
            
            context.fill(path, with: .linearGradient(
                gradient,
                startPoint: outerCube[face[0]].point,
                endPoint: outerCube[face[2]].point
            ))
            
            // Draw edges
            context.stroke(path, with: .color(.cyan.opacity(opacity)), lineWidth: 1.5)
        }
        
        // Draw inner cube faces
        for face in faces {
            let path = Path { p in
                p.move(to: innerCube[face[0]].point)
                for i in 1...3 {
                    p.addLine(to: innerCube[face[i]].point)
                }
                p.closeSubpath()
            }
            
            let avgDepth = face.reduce(0.0) { $0 + innerCube[$1].depth } / 4.0
            let opacity = calculateOpacity(depth: avgDepth)
            
            let gradient = Gradient(colors: [
                Color.blue.opacity(opacity * 0.3),
                Color.cyan.opacity(opacity * 0.2)
            ])
            
            context.fill(path, with: .linearGradient(
                gradient,
                startPoint: innerCube[face[0]].point,
                endPoint: innerCube[face[2]].point
            ))
            
            context.stroke(path, with: .color(.cyan.opacity(opacity)), lineWidth: 1.5)
        }
        
        // Draw connecting lines between cubes
        drawConnections(context: context, from: innerCube, to: outerCube)
    }
    
    /// Draws a cross-section of the tesseract
    private func drawCrossSection(context: GraphicsContext, position: Double, scale: Double, center: CGPoint) {
        // Calculate vertices for the cross-section
        let w = position // Use position as w-coordinate
        let sectionVertices = createVertices(
            scale: 1.0,
            w: w,
            angles: (
                xw: lockXW ? 0 : xwRotation,
                yz: lockYZ ? 0 : yzRotation,
                xy: lockXY ? 0 : xyRotation,
                zw: lockZW ? 0 : zwRotation
            ),
            viewScale: scale,
            center: center
        )
        
        // Draw cross-section as a filled polygon
        let path = Path { p in
            p.move(to: sectionVertices[0].point)
            for i in 1..<8 {
                p.addLine(to: sectionVertices[i].point)
            }
            p.closeSubpath()
        }
        
        // Calculate average depth for the entire section
        let avgDepth = sectionVertices.reduce(0.0) { $0 + $1.depth } / Double(sectionVertices.count)
        let opacity = calculateOpacity(depth: avgDepth)
        
        // Fill with semi-transparent gradient
        let gradient = Gradient(colors: [
            Color.purple.opacity(opacity * 0.3),
            Color.pink.opacity(opacity * 0.2)
        ])
        
        context.fill(path, with: .linearGradient(
            gradient,
            startPoint: CGPoint(x: center.x - scale/2, y: center.y - scale/2),
            endPoint: CGPoint(x: center.x + scale/2, y: center.y + scale/2)
        ))
        
        // Draw outline
        context.stroke(path, with: .color(.purple.opacity(opacity)), lineWidth: 2.0)
    }
}

/// Custom rotation speed control view
struct RotationControl: View {
    let label: String
    @Binding var value: Double
    let description: String
    let primaryColor: Color
    
    var body: some View {
        VStack(alignment: .leading, spacing: 5) {
            HStack {
                Text(label)
                    .font(.subheadline)
                Spacer()
                Text(value == 0 ? "Stop" : String(format: value > 0 ? "+%.1f×" : "%.1f×", value))
                    .font(.caption)
                    .foregroundColor(value == 0 ? .gray : (value > 0 ? .green : .red))
            }
            
            Text(description)
                .font(.caption)
                .foregroundColor(.gray)
            
            Slider(value: $value, in: -2...2)
                .tint(primaryColor)
        }
    }
}

/// Separate view for tesseract controls
struct TesseractControlsView: View {
    @Environment(\.dismiss) private var dismiss
    
    @Binding var visualizationMode: TesseractVisualizationMode
    @Binding var projectionMode: TesseractProjectionMode
    @Binding var isAutoRotating: Bool
    @Binding var autoRotationSpeed: Double
    @Binding var xwRotation: Double
    @Binding var yzRotation: Double
    @Binding var xyRotation: Double
    @Binding var zwRotation: Double
    @Binding var lockXW: Bool
    @Binding var lockYZ: Bool
    @Binding var lockXY: Bool
    @Binding var lockZW: Bool
    @Binding var showCrossSection: Bool
    @Binding var crossSectionPosition: Double
    
    let primaryColor: Color
    let secondaryColor: Color
    let accentColor: Color
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 24) {
                    // Visualization Mode
                    GroupBox {
                        VStack(alignment: .leading) {
                            Text("Visualization")
                                .font(.headline)
                                .foregroundColor(.cyan)
                            
                            HStack(spacing: 20) {
                                Button(action: { visualizationMode = .wireframe }) {
                                    VStack {
                                        Image(systemName: visualizationMode == .wireframe ? "checkmark.circle.fill" : "circle")
                                        Text("Wireframe")
                                    }
                                }
                                .buttonStyle(.bordered)
                                .tint(visualizationMode == .wireframe ? primaryColor : .gray)
                                
                                Button(action: { visualizationMode = .solid }) {
                                    VStack {
                                        Image(systemName: visualizationMode == .solid ? "checkmark.circle.fill" : "circle")
                                        Text("Solid")
                                    }
                                }
                                .buttonStyle(.bordered)
                                .tint(visualizationMode == .solid ? primaryColor : .gray)
                            }
                            .padding(.vertical, 8)
                        }
                    }
                    .groupBoxStyle(DarkGroupBoxStyle())
                    
                    // Projection Mode
                    GroupBox {
                        VStack(alignment: .leading) {
                            Text("Projection")
                                .font(.headline)
                                .foregroundColor(.cyan)
                            
                            HStack(spacing: 20) {
                                Button(action: { projectionMode = .perspective }) {
                                    VStack {
                                        Image(systemName: projectionMode == .perspective ? "checkmark.circle.fill" : "circle")
                                        Text("Perspective")
                                    }
                                }
                                .buttonStyle(.bordered)
                                .tint(projectionMode == .perspective ? primaryColor : .gray)
                                
                                Button(action: { projectionMode = .orthographic }) {
                                    VStack {
                                        Image(systemName: projectionMode == .orthographic ? "checkmark.circle.fill" : "circle")
                                        Text("Orthographic")
                                    }
                                }
                                .buttonStyle(.bordered)
                                .tint(projectionMode == .orthographic ? primaryColor : .gray)
                            }
                            .padding(.vertical, 8)
                        }
                    }
                    .groupBoxStyle(DarkGroupBoxStyle())
                    
                    // Cross-section controls
                    GroupBox {
                        VStack(alignment: .leading) {
                            Text("Cross Section")
                                .font(.headline)
                                .foregroundColor(.cyan)
                            
                            VStack(spacing: 10) {
                                Toggle(isOn: $showCrossSection) {
                                    Label("Show", systemImage: showCrossSection ? "square.stack.3d.down.right.fill" : "square.stack.3d.down.right")
                                }
                                .tint(primaryColor)
                                
                                if showCrossSection {
                                    VStack(alignment: .leading, spacing: 5) {
                                        Text("Position")
                                            .font(.subheadline)
                                            .foregroundColor(.gray)
                                        Slider(value: $crossSectionPosition, in: -1.0...1.0)
                                            .tint(accentColor)
                                    }
                                }
                            }
                            .padding(.vertical, 8)
                        }
                    }
                    .groupBoxStyle(DarkGroupBoxStyle())
                    
                    // Rotation Controls
                    GroupBox {
                        VStack(alignment: .leading, spacing: 15) {
                            Text("Rotation Control")
                                .font(.headline)
                                .foregroundColor(.cyan)
                            
                            // Auto/Manual Toggle
                            Toggle(isOn: $isAutoRotating) {
                                Label(isAutoRotating ? "Uniform Speed" : "Individual Speeds", 
                                      systemImage: isAutoRotating ? "arrow.triangle.2.circlepath" : "slider.horizontal.3")
                            }
                            .tint(primaryColor)
                            
                            if isAutoRotating {
                                // Global Speed Control
                                VStack(alignment: .leading, spacing: 5) {
                                    HStack {
                                        Text("Global Speed")
                                            .font(.subheadline)
                                        Spacer()
                                        Text(String(format: "%.1f×", autoRotationSpeed))
                                            .font(.caption)
                                            .foregroundColor(.gray)
                                    }
                                    
                                    Slider(value: $autoRotationSpeed, in: 0.1...2.0)
                                        .tint(primaryColor)
                                }
                            } else {
                                // Individual Speed Controls
                                VStack(spacing: 15) {
                                    RotationControl(
                                        label: "4D Rotation (XW)",
                                        value: $xwRotation,
                                        description: "Speed through the fourth dimension",
                                        primaryColor: primaryColor
                                    )
                                    
                                    RotationControl(
                                        label: "Height (YZ)",
                                        value: $yzRotation,
                                        description: "Vertical rotation speed",
                                        primaryColor: primaryColor
                                    )
                                    
                                    RotationControl(
                                        label: "Horizontal (XY)",
                                        value: $xyRotation,
                                        description: "Horizontal rotation speed",
                                        primaryColor: primaryColor
                                    )
                                    
                                    RotationControl(
                                        label: "Depth (ZW)",
                                        value: $zwRotation,
                                        description: "Depth rotation speed",
                                        primaryColor: primaryColor
                                    )
                                }
                            }
                        }
                        .padding(.vertical, 8)
                    }
                    .groupBoxStyle(DarkGroupBoxStyle())
                }
                .padding()
            }
            .background(Color.black)
            .navigationTitle("Tesseract Controls")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .foregroundColor(.cyan)
                }
            }
            .preferredColorScheme(.dark)
        }
    }
}

struct DarkGroupBoxStyle: GroupBoxStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack(alignment: .leading) {
            configuration.content
        }
        .padding()
        .background(Color(white: 0.1))
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(LinearGradient(colors: [.blue.opacity(0.3), .cyan.opacity(0.3)], 
                                     startPoint: .topLeading, 
                                     endPoint: .bottomTrailing),
                        lineWidth: 1)
        )
    }
}

#Preview {
    TesseractView(preview: true)
}

// Add this helper class for DisplayLink
private class DisplayLinkTarget: NSObject {
    private let updateAction: () -> Void
    
    init(update: @escaping () -> Void) {
        self.updateAction = update
        super.init()
    }
    
    @objc func handleUpdate() {
        updateAction()
    }
} 
