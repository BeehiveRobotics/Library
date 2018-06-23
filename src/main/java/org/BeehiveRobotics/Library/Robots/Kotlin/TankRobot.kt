package org.BeehiveRobotics.Library.Robots.Kotlin

import org.BeehiveRobotics.Library.Motors.Kotlin.TankDrive
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

class TankRobot(opMode: BROpMode) {
    private val opMode: BROpMode = opMode
    val drive: TankDrive = TankDrive(opMode)
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
    fun waitUntilNotBusy() {
        while(drive.isBusy) {
            if(!opMode.opModeIsActive()) {
                return
            }
        }
    }


}