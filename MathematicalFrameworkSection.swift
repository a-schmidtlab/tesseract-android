    import SwiftUI

struct MathematicalFrameworkSection: View {
    var body: some View {
        Group {
            Text("Mathematical Framework")
                .font(.system(.title2, design: .rounded))
                .foregroundColor(.cyan)
            
            Text("""
            Let me tell you about something fascinating that happens when we start rotating objects in four dimensions. You see, in our everyday three-dimensional world, when we rotate something, we're always rotating around a line – an axis. When you spin a top, it's rotating around a vertical axis. When you open a door, it's rotating around the hinges.

            But in four dimensions, something remarkable happens. Instead of rotating around a line, we rotate through a plane. It's as if we've given our object a whole new kind of freedom to move. And here's the really interesting part: in four dimensions, we have six independent planes we can rotate through, each giving us a different kind of motion:
            """)
            .foregroundColor(.gray)
            
            VStack(alignment: .leading, spacing: 8) {
                Text("• The XY-plane: Like spinning a coin on a table")
                Text("• The XZ-plane: Like opening a door")
                Text("• The XW-plane: A completely new motion impossible in 3D!")
                Text("• The YZ-plane: Like a cartwheel")
                Text("• The YW-plane: Another new 4D motion")
                Text("• The ZW-plane: Yet another motion unique to 4D")
            }
            .foregroundColor(.gray)
            
            Text("""
            When we rotate through any of these planes, something magical happens. The motion looks strange to our three-dimensional eyes because we're seeing a shadow, or projection, of the true four-dimensional rotation. It's similar to how the shadow of a spinning cube on a wall can appear to turn inside out – except now we're dealing with an object that's actually performing a motion that's impossible in our familiar space.

            Let's look at one specific type of rotation, through the XW-plane. The mathematics that describes this motion is captured in what we call a rotation matrix:
            """)
            .foregroundColor(.gray)
            
            Text("Rotation Matrix (XW-plane):")
                .font(.caption)
                .foregroundColor(.gray)
                .padding(.top)
            
            MathFormula(type: .rotationMatrix, size: 150)
                .padding(.vertical)
            
            Text("""
            This matrix might look intimidating, but it's telling us a beautiful story. Each row and column represents how one dimension affects another during the rotation. The sine and cosine terms show how the X and W dimensions smoothly trade places with each other as the rotation progresses, while Y and Z remain unchanged.

            But here's where it gets really interesting. To show this four-dimensional object on your screen, we need to do something clever. We need to transform our four-dimensional coordinates into something your eyes can understand. It's like being a four-dimensional artist trying to paint on a three-dimensional canvas:
            """)
            .foregroundColor(.gray)
            
            MathFormula(type: .quaternion, size: 120)
                .padding(.vertical)
            
            Text("""
            This formula is like a recipe for rotation. The 'cos' and 'sin' parts ensure the rotation is smooth, just like the gentle motion of a pendulum. The 'v' part tells us which way we're rotating.

            And to actually see this on our screen, we need one final transformation:
            """)
            .foregroundColor(.gray)
            
            MathFormula(type: .projection, size: 120)
                .padding(.vertical)
            
            Text("""
            This formula is our window into the fourth dimension. It takes a four-dimensional point (x,y,z,w) and shows us where it should appear in our three-dimensional view. The '1-w' in the denominator is what gives our visualization its characteristic "inside-out" appearance during rotation.
            """)
            .foregroundColor(.gray)
        }
    }
}

#Preview {
    MathematicalFrameworkSection()
        .preferredColorScheme(.dark)
        .padding()
        .background(Color.black)
} 