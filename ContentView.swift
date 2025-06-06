//
//  ContentView.swift
//  SofaRotator
//
//  Created by Axel Schmidt on 10.02.25.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                Color.black.edgesIgnoringSafeArea(.all)
                
                TesseractView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
        }
    }
}

#Preview {
    ContentView()
}
