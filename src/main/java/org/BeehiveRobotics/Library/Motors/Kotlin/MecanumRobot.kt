package org.BeehiveRobotics.Library.Motors.Kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

class MecanumRobot constructor(private val opMode: BROpMode) {
    val drive: MecanumDrive = MecanumDrive(opMode)
    fun init() {
        drive.mapHardware()
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        drive.init()
    }

    fun stop() {
        drive.stopMotors()
    }

    companion object {
        fun sleep(milliseconds: Long) {
            try {
                Thread.sleep(milliseconds)
            } catch (e: Exception) {
            }
        }
    }

}