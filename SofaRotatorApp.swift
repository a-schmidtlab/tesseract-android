//
//  TesseractAxApp.swift
//  TesseractAx
//
//  Created by Axel Schmidt on 10.02.25.
//

import SwiftUI

@main
struct SofaRotatorApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    // Lock orientation to portrait
                    if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                        windowScene.requestGeometryUpdate(.iOS(interfaceOrientations: .portrait))
                    }
                    
                    // Set supported orientations for the entire app
                    if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                        windowScene.keyWindow?.rootViewController?.setNeedsUpdateOfSupportedInterfaceOrientations()
                    }
                }
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        return .portrait
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
            let geometryPreferences = UIWindowScene.GeometryPreferences.iOS()
            geometryPreferences.interfaceOrientations = .portrait
            windowScene.requestGeometryUpdate(geometryPreferences) { error in
                print("Failed to update geometry: \(error)")
            }
        }
        return true
    }
}
