package org.BeehiveRobotics.Library.Sensors.Kotlin

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

class MRGyro(opMode: BROpMode, name: String) {
    private val gyro: ModernRoboticsI2cGyro = opMode.hardwareMap.get(ModernRoboticsI2cGyro::class.java, name)
    private var heading: Int = 0

    public fun calibrate(timeoutSeconds: Double): Boolean {
        val calibrateTime: ElapsedTime = ElapsedTime()
        calibrateTime.reset()
        gyro.calibrate()
        while(gyro.isCalibrating) {
            if(calibrateTime.seconds()> timeoutSeconds) {
                return false
            }
        }
        return true
    }

    public fun calibrate(): Boolean {
        return calibrate(10.toDouble())
    }

    public fun getHeading(): Int {
        this.heading = gyro.heading
        return this.heading
    }

}