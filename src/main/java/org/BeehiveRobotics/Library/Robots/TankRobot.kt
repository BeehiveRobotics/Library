package org.BeehiveRobotics.Library.Robots

import com.qualcomm.robotcore.hardware.DcMotor
import org.BeehiveRobotics.Library.Motors.TankDrive
import org.BeehiveRobotics.Library.Util.BROpMode

class TankRobot(opMode: BROpMode) {
    private val opMode: BROpMode = opMode
    lateinit var drive: TankDrive
    fun init() {
        drive = TankDrive(opMode)
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
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