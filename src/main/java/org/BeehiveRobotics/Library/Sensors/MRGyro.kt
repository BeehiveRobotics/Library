package org.BeehiveRobotics.Library.Sensors

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode

class MRGyro(opMode: BROpMode, name: String) {
    private val gyro: ModernRoboticsI2cGyro = opMode.hardwareMap.get(ModernRoboticsI2cGyro::class.java, name)
    var heading: Int = 0
        private set
        get() = gyro.heading

    public fun calibrate(timeoutSeconds: Int = 10): Boolean {
        val calibrateTime: ElapsedTime = ElapsedTime()
        calibrateTime.reset()
        gyro.calibrate()
        while(gyro.isCalibrating) {
            if(calibrateTime.seconds() > timeoutSeconds) {
                return false
            }
        }
        return true
    }
}