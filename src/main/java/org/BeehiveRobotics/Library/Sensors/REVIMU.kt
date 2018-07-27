package org.BeehiveRobotics.Library.Sensors

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.BeehiveRobotics.Library.Util.BROpMode

class REVIMU(private val opMode: BROpMode) {
    val imu: BNO055IMU = opMode.hardwareMap.get(BNO055IMU::class.java, "imu")
    private var parameters: BNO055IMU.Parameters = BNO055IMU.Parameters()
    var heading = 0.0
        get() = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle.toDouble()
        private set
    init {
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
    }
    fun calibrate(timeoutSeconds: Int = 10): Boolean {
        val time: ElapsedTime = ElapsedTime()
        time.reset()
        while(!imu.initialize(parameters)) {
            if(time.seconds()>timeoutSeconds) {
                return false
            }
        }
        return true
    }
}