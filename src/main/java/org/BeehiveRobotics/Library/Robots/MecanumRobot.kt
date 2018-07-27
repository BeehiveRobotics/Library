package org.BeehiveRobotics.Library.Robots

import com.qualcomm.robotcore.hardware.DcMotor
import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Systems.MecanumDrive

class MecanumRobot(val opMode: BROpMode) {
    lateinit var drive: MecanumDrive
    fun init() {
        drive = MecanumDrive(opMode)
        drive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun stop() {
        drive.stopMotors()
    }

    fun sleep(milliseconds: Long) {
        val time: ElapsedTime = ElapsedTime()
        time.reset()
        while(time.milliseconds() < milliseconds) {
            if(!(opMode.opModeIsActive())) {
                return
            }
        }
    }
    fun waitUntilNotBusy() {
        while(drive.isBusy) {
            if(!(opMode.opModeIsActive())) {
                return
            }
        }
    }

}