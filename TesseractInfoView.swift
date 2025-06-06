import SwiftUI

struct DimensionalProgressionView: View {
    let size: CGFloat
    
    var body: some View {
        Canvas { context, size in
            // Point with glow
            context.fill(Path(ellipseIn: CGRect(x: size.width * 0.1 - 2, y: size.height * 0.4 - 2, width: 8, height: 8)), 
                        with: .color(.blue.opacity(0.3)))
            context.fill(Path(ellipseIn: CGRect(x: size.width * 0.1, y: size.height * 0.4, width: 4, height: 4)), 
                        with: .color(.cyan))
            
            // Line with gradient
            let linePath = Path { p in
                p.move(to: CGPoint(x: size.width * 0.2, y: size.height * 0.4))
                p.addLine(to: CGPoint(x: size.width * 0.35, y: size.height * 0.4))
            }
            let lineGradient = Gradient(colors: [Color.blue, Color.cyan])
            context.stroke(
                linePath,
                with: .linearGradient(
                    lineGradient,
                    startPoint: CGPoint(x: size.width * 0.2, y: size.height * 0.4),
                    endPoint: CGPoint(x: size.width * 0.35, y: size.height * 0.4)
                ),
                lineWidth: 3
            )
            
            // Square with gradient edges
            let squarePath = Path { p in
                p.move(to: CGPoint(x: size.width * 0.45, y: size.height * 0.3))
                p.addLine(to: CGPoint(x: size.width * 0.55, y: size.height * 0.3))
                p.addLine(to: CGPoint(x: size.width * 0.55, y: size.height * 0.5))
                p.addLine(to: CGPoint(x: size.width * 0.45, y: size.height * 0.5))
                p.closeSubpath()
            }
            let squareGradient = Gradient(colors: [Color.blue, Color.cyan])
            context.stroke(
                squarePath,
                with: .linearGradient(
                    squareGradient,
                    startPoint: CGPoint(x: size.width * 0.45, y: size.height * 0.3),
                    endPoint: CGPoint(x: size.width * 0.55, y: size.height * 0.3)
                ),
                lineWidth: 2.5
            )
            
            // Cube with enhanced perspective
            let cubeSize: CGFloat = size.width * 0.15
            let cubeOffset: CGFloat = size.width * 0.7
            let perspective: CGFloat = 0.4
            
            // Front face with gradient
            let frontPath = Path { p in
                p.move(to: CGPoint(x: cubeOffset, y: size.height * 0.5))
                p.addLine(to: CGPoint(x: cubeOffset + cubeSize, y: size.height * 0.5))
                p.addLine(to: CGPoint(x: cubeOffset + cubeSize, y: size.height * 0.3))
                p.addLine(to: CGPoint(x: cubeOffset, y: size.height * 0.3))
                p.closeSubpath()
            }
            let cubeGradient = Gradient(colors: [Color.blue, Color.cyan])
            context.stroke(
                frontPath,
                with: .linearGradient(
                    cubeGradient,
                    startPoint: CGPoint(x: cubeOffset, y: size.height * 0.3),
                    endPoint: CGPoint(x: cubeOffset + cubeSize, y: size.height * 0.3)
                ),
                lineWidth: 2.5
            )
            
            // Back edges with glow
            let backPath = Path { p in
                p.move(to: CGPoint(x: cubeOffset, y: size.height * 0.3))
                p.addLine(to: CGPoint(x: cubeOffset + cubeSize * perspective, y: size.height * 0.2))
                p.move(to: CGPoint(x: cubeOffset + cubeSize, y: size.height * 0.3))
                p.addLine(to: CGPoint(x: cubeOffset + cubeSize + cubeSize * perspective, y: size.height * 0.2))
                p.move(to: CGPoint(x: cubeOffset + cubeSize * perspective, y: size.height * 0.2))
                p.addLine(to: CGPoint(x: cubeOffset + cubeSize + cubeSize * perspective, y: size.height * 0.2))
            }
            
            context.stroke(backPath, with: .color(.blue.opacity(0.6)), lineWidth: 1.5)
        }
        .frame(width: size, height: size * 0.6)
        .background(Color.black.opacity(0.3))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}

struct LatexStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .font(.custom("Courier", size: 14))
            .padding(16)
            .background(Color(white: 0.1))
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.blue.opacity(0.3), lineWidth: 1)
            )
            .shadow(color: .blue.opacity(0.1), radius: 5, x: 0, y: 2)
    }
}

struct IntroductionSection: View {
    var body: some View {
        Group {
            Text("Welcome to the 4th Dimension")
                .font(.system(.title, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            Have you ever wondered what it would be like to have an extra dimension? We live in a world with three dimensions – up/down, left/right, and forward/backward. Every movement you make, every object you see, exists within these three dimensions. But what if there was more?

            Think about this: If you draw a square on a piece of paper, that's a two-dimensional shape. Now, if you pull that square up off the paper, you create a cube – a three-dimensional shape. The cube is what you get when you extend a square into a new dimension.

            Consider, then, the question that follows: What happens when you extend a cube into a fourth dimension? This is what we are going to explore. The shape you get is called a tesseract or hypercube, and while we cannot physically see it (just as a flat paper creature could not see a full cube), we can understand it through its three-dimensional shadow – which is what you are seeing in this visualization.

            It is akin to being a detective, piecing together clues about something we cannot directly see. Just as we can understand a cube by looking at its shadows or its flat drawings, we can understand a tesseract by watching how it casts its three-dimensional shadow as it rotates through four-dimensional space.

            What makes this deeply compelling is that it challenges our everyday experience. When you see the tesseract rotating, you are witnessing something truly desturbing – a glimpse into a reality that exists mathematically but lies just beyond our physical senses. It is a window into a world where new kinds of movement are possible, where inside and outside take on new meanings, and where our usual rules about space need to be reunderstood.

            As we explore deeper, we will see:
            • How mathematicians first discovered and understood the fourth dimension
            • What new types of movement become possible in four dimensions
            • How we can use mathematics to "see" what we cannot physically observe
            • Why understanding higher dimensions matters in modern science

            It is natural if this seems mind-bending at first. Even some mathematicians needed time to wrap their heads around these concepts. The key is to let your imagination work alongside the mathematics, building a bridge between what we can see and what we can understand.

            Are you prepared to begin this journey into the fourth dimension?
            """)
            .font(.system(.body, design: .rounded))
        }
    }
}

struct DimensionalAnalysisSection: View {
    var body: some View {
        Group {
            Text("Dimensional Analysis")
                .font(.system(.title2, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            Let's take a journey through dimensions, starting from the simplest and building our way up to the tesseract.
            """)
            .foregroundColor(.gray)
            
            DimensionalProgressionView(size: 300)
                .padding(.vertical)
            
            Text("""
            • Point (0D): A single location in space
            • Line (1D): A point extended into length
            • Square (2D): A line extended into width
            • Cube (3D): A square extended into height
            • Tesseract (4D): A cube extended into the fourth dimension
            
            Each step follows the same pattern: take the previous shape and extend it in a new direction, perpendicular to all previous dimensions. The tesseract follows this pattern into the fourth dimension, even though we can't directly visualize this extension.
            """)
            .foregroundColor(.gray)
        }
    }
}

struct HistoricalSection: View {
    var body: some View {
        Group {
            Text("Historical Journey")
                .font(.system(.title2, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            The concept of the fourth dimension has a rich history in both mathematics and culture:
            
            1800s: The mathematical foundations
            • Charles Howard Hinton coined the term "tesseract" in 1888
            • Edwin Abbott Abbott published "Flatland" in 1884, exploring dimensional concepts through story
            
            1900s: Scientific revolution
            • Einstein's theory of relativity introduced time as the fourth dimension
            • Marcel Duchamp's "Nude Descending a Staircase" (1912) attempted to show 4D in art
            
            Modern era:
            • Computer graphics allow us to visualize higher dimensions
            • String theory suggests our universe might have 10 or 11 dimensions
            • Quantum computing uses multi-dimensional mathematics
            
            The tesseract has become more than just mathematics – it's a symbol of how we push the boundaries of human understanding.
            """)
            .foregroundColor(.gray)
            
            // Add a stylized quote
            Text("\"The only way of discovering the limits of the possible is to venture a little way past them into the impossible.\" - Arthur C. Clarke")
                .italic()
                .padding()
                .frame(maxWidth: .infinity)
                .background(Color(white: 0.1))
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(LinearGradient(colors: [.blue.opacity(0.3), .cyan.opacity(0.3)], 
                                             startPoint: .topLeading, 
                                             endPoint: .bottomTrailing),
                                lineWidth: 1)
                )
                .padding(.vertical)
                .foregroundColor(.gray)
        }
    }
}

struct TechnicalSection: View {
    var body: some View {
        Group {
            Text("Behind the Scenes")
                .font(.system(.title2, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            Let me show you something fascinating about how dimensions build upon each other:
            """)
            .foregroundColor(.gray)
            
            // Dimensional progression display
            VStack(alignment: .leading, spacing: 12) {
                Text("Building Up Through Dimensions:")
                    .font(.caption)
                    .foregroundColor(.gray)
                
                Text("""
                0D:  Point
                     ↓  + length
                1D:  Line
                     ↓  + width
                2D:  Square
                     ↓  + height
                3D:  Cube
                     ↓  + ana
                4D:  Tesseract
                """)
                .font(.custom("Courier", size: 16))
                .tracking(2)
                .foregroundColor(.cyan)
                .padding(20)
                .background(Color(white: 0.1))
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(LinearGradient(colors: [.blue, .cyan], 
                                             startPoint: .topLeading, 
                                             endPoint: .bottomTrailing),
                                lineWidth: 1)
                )
                .shadow(color: .blue.opacity(0.2), radius: 8, x: 0, y: 4)
            }
            .padding(.vertical)
            
            Text("""
            This progression reveals the fundamental pattern of dimensional thinking. Each new dimension adds a new direction of movement, perpendicular to all previous ones. The mysterious fourth dimension (sometimes called 'ana') extends the cube in a direction we can't point to in our 3D world, just as we can't point to 'up' while staying on a 2D surface.

            Now, let me show you something really cool about how we make this four-dimensional object dance on your screen.
            """)
            .foregroundColor(.gray)
            
            Text("""
            First, we use something called quaternions. Don't let the fancy name scare you – they're actually a beautiful way to handle rotations. Imagine you're trying to tell someone how to rotate an object. You could give them a sequence of steps: "First rotate this way, then that way..." But quaternions let us describe any rotation in one elegant package:
            """)
            .foregroundColor(.gray)
            
            MathFormula(type: .quaternion, size: 60)
                .padding(.vertical)
            
            Text("""
            This formula is like a recipe for rotation. The 'cos' and 'sin' parts ensure the rotation is smooth, just like the gentle motion of a pendulum. The 'v' part tells us which way we're rotating.

            To show this four-dimensional object on your screen, we need to do something clever. We need to transform our four-dimensional coordinates into something your eyes can understand. It's like being a four-dimensional artist trying to paint on a three-dimensional canvas:
            """)
            .foregroundColor(.gray)
            
            MathFormula(type: .dimensionalMapping, size: 150)
                .padding(.vertical)
            
            Text("""
            Each arrow in this diagram represents a transformation, like a series of magical lenses that each show us a different view of our four-dimensional friend. The first lens takes our four-dimensional object and prepares it for projection. The second lens squeezes it into three dimensions, and the final lens flattens it onto your screen.

            The tesseract is quite a complex fellow with lots of parts:
            """)
            .foregroundColor(.gray)
            
            MathFormula(type: .vertexCount, size: 150)
                .padding(.vertical)
            
            Text("""
            Each of these numbers tells a story. The 16 vertices are like the corners of our four-dimensional cube, connected by 32 edges. These edges form 24 faces, which in turn make up 8 cubic cells. It's like having eight regular cubes that are all connected in a way that makes perfect sense in four dimensions, even though it looks mind-bending in our three-dimensional view.

            When you watch the tesseract rotate, you're seeing all these parts move together in a carefully choreographed dance, each following the precise mathematics we've laid out.
            """)
            .foregroundColor(.gray)
        }
    }
}

struct InteractiveSection: View {
    var body: some View {
        Group {
            Text("A Window into the Fourth Dimension")
                .font(.system(.title2, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            Now comes the most exciting part – actually watching our four-dimensional friend in action. As you observe the tesseract rotating, you're seeing something truly remarkable: a three-dimensional shadow of a four-dimensional dance.

            Let me help you understand what you're seeing. Imagine you're a two-dimensional being watching the shadow of a cube rotating on your flat world. As the cube rotates, its shadow would stretch, shrink, and seem to turn inside out in ways that might seem impossible in your flat world. That's exactly what's happening here, just one dimension higher!

            Here's what to look for in the visualization:

            The Inner Cube:
            This is like your anchor point – it's a three-dimensional slice of the tesseract at a particular moment. Think of it as if you're taking a three-dimensional "photograph" of a four-dimensional object. As the tesseract rotates, this inner cube changes shape because we're seeing different three-dimensional cross-sections of the four-dimensional whole.

            The Outer Structure:
            Those seemingly floating lines and faces that appear to bend impossibly? They're showing you how the tesseract extends into the fourth dimension. When they appear to intersect or pass through each other, remember: in four dimensions, they don't intersect at all! It's just like how the edges of a cube's shadow might appear to cross on a flat surface, even though in three dimensions they're nowhere near each other.

            The Connecting Lines:
            These are like your guides through the fourth dimension. They show you how each point in the inner cube connects to its corresponding point in the outer structure. As the tesseract rotates, these lines trace out the paths that connect corresponding points across the fourth dimension.

            The "Inside-Out" Motion:
            This is perhaps the most mind-bending part. When you see parts of the tesseract appear to turn inside out, you're witnessing what happens when we try to squeeze a four-dimensional rotation into three dimensions. It's like trying to show the back of a cube without lifting it off the page – something has to give.

            Remember, what makes this visualization so special is not just what it shows, but what it suggests about the nature of space itself. Every time you see the tesseract seem to do something impossible, you're catching a glimpse of the extra freedom that comes with having four spatial dimensions.

            Take your time with it. Let your mind play with the possibilities. And don't worry if it seems confusing at first – you're literally training your brain to think beyond its everyday three-dimensional experience. That's not just mathematics; that's adventure.
            """)
            .foregroundColor(.gray)
        }
    }
}

struct ContentSelectionView: View {
    let onSelectGuide: () -> Void
    let onSelectMath: () -> Void
    
    var body: some View {
        VStack(spacing: 32) {
            VStack(spacing: 24) {
                Button(action: onSelectGuide) {
                    VStack(spacing: 12) {
                        Image(systemName: "hand.tap")
                            .font(.largeTitle)
                        Text("App Guide")
                            .font(.headline)
                        Text("Learn how to use the app's features and controls")
                            .font(.caption)
                            .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(24)
                    .background(Color(white: 0.1))
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(LinearGradient(colors: [.blue, .cyan], 
                                                 startPoint: .topLeading, 
                                                 endPoint: .bottomTrailing),
                                    lineWidth: 1)
                    )
                }
                
                Button(action: onSelectMath) {
                    VStack(spacing: 12) {
                        Image(systemName: "cube.transparent")
                            .font(.largeTitle)
                        Text("Mathematical Journey")
                            .font(.headline)
                        Text("Explore the mathematics and history of the fourth dimension")
                            .font(.caption)
                            .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(24)
                    .background(Color(white: 0.1))
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(LinearGradient(colors: [.purple, .cyan], 
                                                 startPoint: .topLeading, 
                                                 endPoint: .bottomTrailing),
                                    lineWidth: 1)
                    )
                }
            }
            .foregroundColor(.cyan)
            .padding(.horizontal)
        }
        .padding(24)
    }
}

struct GuideImageView: View {
    let imageName: String
    let caption: String
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Image(imageName)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxHeight: 120)
                .cornerRadius(8)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(LinearGradient(colors: [.blue.opacity(0.3), .cyan.opacity(0.3)], 
                                             startPoint: .topLeading, 
                                             endPoint: .bottomTrailing),
                                lineWidth: 1)
                )
            
            if !caption.isEmpty {
                Text(caption)
                    .font(.caption)
                    .foregroundColor(.gray)
            }
        }
        .padding(.vertical, 8)
    }
}

struct AppGuideSection: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 24) {
            Text("App Guide")
                .font(.system(.title, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("This guide will help you explore and understand four-dimensional geometry through interactive visualization.")
                .font(.system(.body, design: .rounded))
                .foregroundColor(.gray)
            
            VStack(alignment: .leading, spacing: 16) {
                Text("Visualization Settings")
                    .font(.headline)
                    .foregroundColor(.cyan)
                
                GuideImageView(
                    imageName: "visualization-controls",
                    caption: "Controls for different visualization modes"
                )
                
                Text("""
                View Modes:
                • **Wireframe**: Shows the edges and connections clearly, helping you understand the structure's geometry and how the vertices connect in 4D space.
                • **Solid**: Displays faces with transparency, giving you a better sense of depth and volume as the tesseract rotates.
                
                Projection Types:
                • **Perspective**: Provides a more natural, 3D-like view similar to how we see objects in our world. Closer parts appear larger.
                • **Orthographic**: Maintains parallel lines and true proportions, useful for understanding the mathematical relationships between different parts.
                
                Cross-Section:
                • Toggle to **slice** through the tesseract and see its internal structure
                • Slider controls where you **cut through** the 4D object
                • Helps visualize how a **3D cube** is just a slice of a **4D tesseract**, similar to how a **2D square** is a slice of a **3D cube**
                """)
                .foregroundColor(.gray)
            }
            
            VStack(alignment: .leading, spacing: 16) {
                Text("Understanding 4D Rotation")
                    .font(.headline)
                    .foregroundColor(.cyan)
                
                GuideImageView(
                    imageName: "rotation-planes",
                    caption: "Controls for rotation in different dimensional planes"
                )
                
                Text("""
                The tesseract rotates in four different planes, each showing a unique aspect of 4D space:
                
                **XW (4D Movement)**:
                • Rotates between our **3D space** and the **fourth dimension**
                • Watch how parts of the tesseract appear to **grow** and **shrink** as they move through 4D
                • Similar to how a cube would appear to a **2D being** as it passes through their plane
                
                **YZ (Height)**:
                • Traditional **vertical rotation**
                • Helps maintain **spatial orientation** while exploring 4D movement
                
                **XY (Horizontal)**:
                • Classic **side-to-side rotation**
                • Provides a **familiar reference** point for understanding more complex rotations
                
                **ZW (Depth)**:
                • Combines **3D depth** with **4D movement**
                • Shows how objects can rotate "**through**" the fourth dimension
                
                For each rotation plane:
                • **Lock toggle**: Freeze specific rotations to focus on understanding one type of movement at a time
                • **Speed slider**: Control how fast each rotation happens, allowing you to study specific movements in detail
                """)
                .foregroundColor(.gray)
            }
            
            VStack(alignment: .leading, spacing: 16) {
                Text("Learning Strategy")
                    .font(.headline)
                    .foregroundColor(.cyan)
                
                Text("""
                1. Start in **wireframe mode** with only **XY rotation** to get comfortable with basic movement
                2. Add **YZ rotation** to understand how the structure moves in 3D space
                3. Enable **XW rotation** to begin exploring the fourth dimension
                4. Use the **cross-section view** to understand how 3D slices form the complete 4D object
                5. Switch to **solid mode** to see how the structure's volume changes during 4D rotation
                6. Experiment with **different combinations** of rotations to develop 4D spatial intuition
                """)
                .foregroundColor(.gray)
            }
        }
        .padding(24)
    }
}

struct TesseractInfoView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var selectedContent: ContentType? = nil
    
    enum ContentType {
        case guide
        case math
    }
    
    var body: some View {
        NavigationView {
            ScrollView {
                if selectedContent == nil {
                    ContentSelectionView(
                        onSelectGuide: { selectedContent = .guide },
                        onSelectMath: { selectedContent = .math }
                    )
                } else if selectedContent == .guide {
                    AppGuideSection()
                        .padding(24)
                } else {
                    VStack(alignment: .leading, spacing: 32) {
                        IntroductionSection()
                        DimensionalAnalysisSection()
                        HistoricalSection()
                        TechnicalSection()
                        InteractiveSection()
                        
                        Text("\"In mathematics you don't understand things. You just get used to them. The essence of mathematics lies in its freedom. Mathematics, viewed this way isn't just about numbers and equations, but about patterns - patterns found in nature, patterns invented by the human mind. It's about how these patterns influence each other, combine and generate new patterns. That's where its true beauty lies.\" - John von Neumann")
                            .italic()
                            .padding()
                            .frame(maxWidth: .infinity)
                            .foregroundColor(.gray)
                        
                        Divider()
                            .background(LinearGradient(colors: [.blue.opacity(0.3), .cyan.opacity(0.3)], 
                                                     startPoint: .leading, 
                                                     endPoint: .trailing))
                            .padding(.horizontal)
                            .padding(.vertical, 8)
                        
                        VStack(alignment: .trailing, spacing: 8) {
                            Text("This App is for Silke")
                                .foregroundColor(.gray)
                            Text("So Long, and Thanks for All the Fish")
                                .italic()
                                .foregroundColor(.gray)
                            Text("- Axel Schmidt (Berlin, 2025)")
                                .foregroundColor(.gray)
                        }
                        .frame(maxWidth: .infinity, alignment: .trailing)
                        .padding(.horizontal)
                    }
                    .padding(24)
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    if selectedContent != nil {
                        Button("Back") {
                            selectedContent = nil
                        }
                        .foregroundColor(.cyan)
                    }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .foregroundColor(.cyan)
                }
            }
            .background(Color.black)
            .preferredColorScheme(.dark)
        }
    }
}

#Preview {
    TesseractInfoView()
        .preferredColorScheme(.dark)
} 