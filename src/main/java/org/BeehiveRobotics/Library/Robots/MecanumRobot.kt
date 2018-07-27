package org.BeehiveRobotics.Library.Robots

import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.MecanumDrive

class MecanumRobot(private val opMode: BROpMode): Robot(opMode) {
    lateinit var drive: MecanumDrive
    fun init() {
        drive = MecanumDrive(opMode)
    }
}