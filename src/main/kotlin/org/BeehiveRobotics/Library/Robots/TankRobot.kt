package org.BeehiveRobotics.Library.Robots

import org.BeehiveRobotics.Library.Systems.TankDrive
import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.DriveMotorSystem

class TankRobot(private val opMode: BROpMode, private val gearedType: DriveMotorSystem.GearedType = DriveMotorSystem.GearedType.NORMAL): Robot(opMode) {
    lateinit var drive: TankDrive
    override fun init() {
        drive = TankDrive(opMode, gearedType)
        drive.onInit()
    }
    override fun waitUntilNotBusy() {
        while(opMode.opModeIsActive() && drive.isBusy) {}
    }

}