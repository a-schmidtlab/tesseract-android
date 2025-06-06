import SwiftUI

struct IconGenerator: View {
    var body: some View {
        ZStack {
            // Background
            Color.black
            
            // Tesseract icon
            Canvas { context, size in
                let center = CGPoint(x: size.width/2, y: size.height/2)
                let scale = min(size.width, size.height) * 0.4
                
                // Create cube vertices
                let cube = [
                    CGPoint(x: center.x - scale/2, y: center.y - scale/2),
                    CGPoint(x: center.x + scale/2, y: center.y - scale/2),
                    CGPoint(x: center.x + scale/2, y: center.y + scale/2),
                    CGPoint(x: center.x - scale/2, y: center.y + scale/2)
                ]
                
                // Create offset cube
                let offset: CGFloat = scale * 0.2
                let offsetCube = cube.map { point in
                    CGPoint(x: point.x + offset, y: point.y + offset)
                }
                
                // Draw cubes
                for i in 0..<cube.count {
                    let nextIndex = (i + 1) % cube.count
                    
                    // Draw first cube
                    let path1 = Path { p in
                        p.move(to: cube[i])
                        p.addLine(to: cube[nextIndex])
                    }
                    
                    // Draw second cube
                    let path2 = Path { p in
                        p.move(to: offsetCube[i])
                        p.addLine(to: offsetCube[nextIndex])
                    }
                    
                    // Draw connections
                    let path3 = Path { p in
                        p.move(to: cube[i])
                        p.addLine(to: offsetCube[i])
                    }
                    
                    context.stroke(path1, with: .color(.blue), lineWidth: 8)
                    context.stroke(path2, with: .color(.blue), lineWidth: 8)
                    context.stroke(path3, with: .color(.blue), lineWidth: 8)
                }
            }
        }
        .frame(width: 1024, height: 1024)
    }
}

#Preview {
    IconGenerator()
} 