package org.BeehiveRobotics.Library.Sensors

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.BeehiveRobotics.Library.Util.BROpMode

class REVIMU(private val opMode: BROpMode) {
    private val imu: BNO055IMU = opMode.hardwareMap.get(BNO055IMU::class.java, "imu")
    private var parameters: BNO055IMU.Parameters = BNO055IMU.Parameters()
    private var isInitialized: Boolean = false
    init {
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
    }
    private fun init() {
        isInitialized = imu.initialize(parameters)
    }
    fun calibrate() {
        val time: ElapsedTime = ElapsedTime()
        time.reset()
        while(!isInitialized) {
            if(time.seconds()>10) {
                break;
            }
            init()
        }
    }
    fun getHeading(): Double {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle.toDouble()
    }

}