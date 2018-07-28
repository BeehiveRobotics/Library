package org.BeehiveRobotics.Library.Robots

import org.BeehiveRobotics.Library.Systems.TankDrive
import org.BeehiveRobotics.Library.Util.BROpMode

class TankRobot(private val opMode: BROpMode): Robot(opMode) {
    lateinit var drive: TankDrive
    fun init() {
        drive = TankDrive(opMode)
    }
    override fun waitUntilNotBusy() {
        while(opMode.opModeIsActive() && drive.isBusy) {}
    }

}