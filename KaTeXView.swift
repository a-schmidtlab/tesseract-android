import SwiftUI

struct KaTeXView: View {
    let expression: String
    let displayStyle: Bool
    
    init(expression: String, displayStyle: Bool = false) {
        self.expression = expression
        self.displayStyle = displayStyle
    }
    
    var body: some View {
        Text(renderKaTeX(expression))
            .font(.custom("Courier", size: displayStyle ? 16 : 14))
            .tracking(1.2)
            .padding(displayStyle ? 20 : 8)
            .background(Color(white: 0.1))
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(LinearGradient(colors: [.blue.opacity(0.3), .cyan.opacity(0.3)], 
                                         startPoint: .topLeading, 
                                         endPoint: .bottomTrailing),
                            lineWidth: 1)
            )
            .shadow(color: .blue.opacity(0.1), radius: 5, x: 0, y: 2)
    }
    
    private func renderKaTeX(_ input: String) -> String {
        // Convert common mathematical notations to Unicode
        var result = input
        
        // Greek letters
        result = result.replacingOccurrences(of: "\\theta", with: "θ")
        result = result.replacingOccurrences(of: "\\alpha", with: "α")
        result = result.replacingOccurrences(of: "\\beta", with: "β")
        result = result.replacingOccurrences(of: "\\gamma", with: "γ")
        
        // Operators and symbols
        result = result.replacingOccurrences(of: "\\times", with: "×")
        result = result.replacingOccurrences(of: "\\cdot", with: "·")
        result = result.replacingOccurrences(of: "\\rightarrow", with: "→")
        result = result.replacingOccurrences(of: "\\leftarrow", with: "←")
        result = result.replacingOccurrences(of: "\\infty", with: "∞")
        result = result.replacingOccurrences(of: "\\partial", with: "∂")
        
        // Brackets
        result = result.replacingOccurrences(of: "\\left(", with: "(")
        result = result.replacingOccurrences(of: "\\right)", with: ")")
        result = result.replacingOccurrences(of: "\\left[", with: "[")
        result = result.replacingOccurrences(of: "\\right]", with: "]")
        result = result.replacingOccurrences(of: "\\left\\{", with: "{")
        result = result.replacingOccurrences(of: "\\right\\}", with: "}")
        
        // Subscripts and superscripts
        result = result.replacingOccurrences(of: "_", with: "₍")
        result = result.replacingOccurrences(of: "^", with: "⁽")
        
        // Mathematical sets
        result = result.replacingOccurrences(of: "\\mathbb{R}", with: "ℝ")
        result = result.replacingOccurrences(of: "\\mathbb{Z}", with: "ℤ")
        result = result.replacingOccurrences(of: "\\mathbb{N}", with: "ℕ")
        result = result.replacingOccurrences(of: "\\mathbb{Q}", with: "ℚ")
        
        // Fractions
        result = result.replacingOccurrences(of: "\\frac", with: "")
        result = result.replacingOccurrences(of: "{", with: "")
        result = result.replacingOccurrences(of: "}", with: "")
        
        return result
    }
}

#Preview {
    VStack(spacing: 20) {
        KaTeXView(expression: "\\mathbb{R}^4 \\rightarrow \\mathbb{R}^3")
        KaTeXView(expression: "\\frac{x}{1-w} + \\frac{y}{1-w} + \\frac{z}{1-w}", displayStyle: true)
        KaTeXView(expression: "q = \\cos(\\theta/2) + v\\cdot\\sin(\\theta/2)")
    }
    .padding()
    .background(Color.black)
    .preferredColorScheme(.dark)
} 