import SwiftUI

struct GuideImages {
    static var visualizationControls: some View {
        VStack(spacing: 12) {
            // Mode picker
            Picker("Mode", selection: .constant(0)) {
                Text("Wireframe").tag(0)
                Text("Solid").tag(1)
            }
            .pickerStyle(.segmented)
            
            // Projection picker
            Picker("Projection", selection: .constant(0)) {
                Text("Perspective").tag(0)
                Text("Orthographic").tag(1)
            }
            .pickerStyle(.segmented)
            
            // Cross section
            VStack(alignment: .leading) {
                Toggle("Cross Section", isOn: .constant(false))
                Slider(value: .constant(0.5))
            }
        }
        .padding()
        .background(Color(white: 0.1))
        .cornerRadius(12)
    }
    
    static var rotationPlanes: some View {
        VStack(spacing: 16) {
            ForEach(["XW (4D)", "YZ (Height)", "XY (Horizontal)", "ZW (Depth)"], id: \.self) { plane in
                HStack {
                    Toggle("", isOn: .constant(false))
                        .labelsHidden()
                    Text(plane)
                    Spacer()
                    Slider(value: .constant(0.5))
                        .frame(width: 100)
                }
            }
        }
        .padding()
        .background(Color(white: 0.1))
        .cornerRadius(12)
    }
}

// Helper view to capture images
struct ImageCapture: View {
    var body: some View {
        VStack(spacing: 20) {
            GuideImages.visualizationControls
                .frame(width: 300)
            
            GuideImages.rotationPlanes
                .frame(width: 300)
        }
        .padding()
        .preferredColorScheme(.dark)
    }
}

#Preview {
    ImageCapture()
}