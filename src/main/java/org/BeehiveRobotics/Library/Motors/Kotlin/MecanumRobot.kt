package org.BeehiveRobotics.Library.Motors.Kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime

class MecanumRobot constructor(opMode: BROpMode) {
    val opMode: BROpMode = opMode
    val drive: MecanumDrive = MecanumDrive(opMode)
    fun init() {
        drive.mapHardware()
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        drive.init()
    }

    fun stop() {
        drive.stopMotors()
    }

    //companion object {
        fun sleep(milliseconds: Long) {
            val time: ElapsedTime = ElapsedTime()
            time.reset()
            while(time.milliseconds() < milliseconds) {
                if(!(opMode.opModeIsActive())) {
                    return
                }
            }
        }
    //}

}