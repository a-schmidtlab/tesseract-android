import CoreMotion
import SwiftUI

class MotionManager: ObservableObject {
    private let motionManager = CMMotionManager()
    @Published var rotation: Double = 0.0
    
    init() {
        startMonitoringMotion()
    }
    
    private func startMonitoringMotion() {
        if motionManager.isDeviceMotionAvailable {
            motionManager.deviceMotionUpdateInterval = 1.0 / 60.0
            motionManager.startDeviceMotionUpdates(to: .main) { [weak self] motion, error in
                guard let motion = motion else { return }
                
                // Convert attitude to degrees and update rotation
                let rotation = motion.attitude.yaw * 180 / .pi
                self?.rotation = rotation
            }
        }
    }
    
    deinit {
        motionManager.stopDeviceMotionUpdates()
    }
} 