package org.BeehiveRobotics.Library.Robots

import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.MecanumDrive
import org.BeehiveRobotics.Library.Systems.DriveMotorSystem

class MecanumRobot(private val opMode: BROpMode, private val gearedType: DriveMotorSystem.GearedType = DriveMotorSystem.GearedType.NORMAL): Robot(opMode) {
    lateinit var drive: MecanumDrive
    override fun init() {
        drive = MecanumDrive(opMode, gearedType)
        drive.init()
    }
    override fun waitUntilNotBusy() {
        while(opMode.opModeIsActive() && drive.isBusy) {}
    }
}