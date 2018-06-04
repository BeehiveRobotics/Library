package org.firstinspires.ftc.teamcode

import org.BeehiveRobotics.Library.Motors.Kotlin.MecanumDrive
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

class MecanumRobot(opMode: BROpMode) {
    private val opMode: BROpMode = opMode
    val drive: MecanumDrive = MecanumDrive(opMode)
    fun init() {
        drive.mapHardware()
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