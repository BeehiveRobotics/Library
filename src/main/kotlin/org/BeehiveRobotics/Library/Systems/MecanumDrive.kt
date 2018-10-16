package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Motors.Motor
import com.qualcomm.robotcore.util.Range

@Suppress("NAME_SHADOWING")
class MecanumDrive(private val opMode: BROpMode, gearedType: DriveMotorSystem.GearedType = GearedType.NORMAL, gearRatio: Double = 1.0): DriveMotorSystem(opMode, gearedType, gearRatio) {
    fun drive(x: Double, y: Double, z: Double, inches: Double, waitForCompletion: Boolean = true) {
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        super.drive(flSpeed, frSpeed, rlSpeed, rrSpeed, inches, waitForCompletion)
    }

    fun drive(x: Double, y: Double, z: Double) {
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        super.setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
    }

    fun drive(xLeft: Double, yLeft: Double, xRight: Double, yRight: Double) {
        val flSpeed: Double = clip(yLeft + xLeft)
        val frSpeed: Double = clip(yRight - xRight)
        val rlSpeed: Double = clip(yLeft - xLeft)
        val rrSpeed: Double = clip(yRight + xRight)
        super.setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
    }

    fun rightGyro(x: Double, y: Double, z: Double, target: Double, waitForCompletion: Boolean = true) {
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        super.rightGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, waitForCompletion)
    }
    fun rightGyro(speed: Double, target: Double, waitForCompletion: Boolean = true) {
        val flSpeed: Double = Math.abs(speed)
        val frSpeed: Double = -Math.abs(speed)
        val rlSpeed: Double = Math.abs(speed)
        val rrSpeed: Double = -Math.abs(speed)
        super.rightGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, waitForCompletion)
    }

    fun leftGyro(x: Double, y: Double, z: Double, target: Double, waitForCompletion: Boolean = true) {
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        super.leftGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, waitForCompletion)
    }
    fun leftGyro(speed: Double, target: Double, waitForCompletion: Boolean = true) {
        val flSpeed: Double = -Math.abs(speed)
        val frSpeed: Double = Math.abs(speed)
        val rlSpeed: Double = -Math.abs(speed)
        val rrSpeed: Double = Math.abs(speed)
        super.rightGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, waitForCompletion)
    }

    fun forward      (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, Math.abs(speed), 0.0, inches, waitForCompletion)
    fun backward     (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, -Math.abs(speed), 0.0, inches, waitForCompletion)
    fun strafeLeft   (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(-Math.abs(speed), 0.0, 0.0, inches, waitForCompletion)
    fun strafeRight  (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(Math.abs(speed), 0.0, 0.0, inches, waitForCompletion)
    fun spinLeft     (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, 0.0, -Math.abs(speed), inches, waitForCompletion)
    fun spinRight    (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, 0.0, Math.abs(speed), inches, waitForCompletion)
    fun leftForward  (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, 0.5*Math.abs(speed), 0.5*Math.abs(speed), inches, waitForCompletion)
    fun leftBackward (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, -0.5*Math.abs(speed), -0.5*Math.abs(speed), inches, waitForCompletion)
    fun rightForward (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, 0.5*Math.abs(speed), -0.5*Math.abs(speed), inches, waitForCompletion)
    fun rightBackward(speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(0.0, -0.5*Math.abs(speed), 0.5*Math.abs(speed), inches, waitForCompletion)
    fun forwardLeft  (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(-Math.abs(speed), Math.abs(speed), 0.0, inches, waitForCompletion)
    fun forwardRight (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(Math.abs(speed), Math.abs(speed), 0.0, inches, waitForCompletion)
    fun backwardLeft (speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(-Math.abs(speed), -Math.abs(speed), 0.0, inches, waitForCompletion)
    fun backwardRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) = this.drive(Math.abs(speed), -Math.abs(speed), 0.0, inches, waitForCompletion)

    fun forward(speed: Double) = this.drive(0.0, Math.abs(speed), 0.0)
    fun backward(speed: Double) = this.drive(0.0, -Math.abs(speed), 0.0)
    fun strafeLeft(speed: Double) = this.drive(-Math.abs(speed), 0.0, 0.0)
    fun strafeRight(speed: Double) = this.drive(Math.abs(speed), 0.0, 0.0)
    fun spinLeft(speed: Double) = this.drive(0.0, 0.0, -Math.abs(speed))
    fun spinRight(speed: Double) = this.drive(0.0, 0.0, Math.abs(speed))
    fun leftForward(speed: Double) = this.drive(0.0, 0.5*Math.abs(speed), 0.5*Math.abs(speed))
    fun leftBackward(speed: Double) = this.drive(0.0, -0.5*Math.abs(speed), -0.5*Math.abs(speed))
    fun rightForward(speed: Double) = this.drive(0.0, 0.5*Math.abs(speed), -0.5*Math.abs(speed))
    fun rightBackward(speed: Double) = this.drive(0.0, -0.5*Math.abs(speed), 0.5*Math.abs(speed))
    fun forwardLeft(speed: Double) = this.drive(-Math.abs(speed), Math.abs(speed), 0.0)
    fun forwardRight(speed: Double) = this.drive(Math.abs(speed), Math.abs(speed), 0.0)
    fun backwardLeft(speed: Double) = this.drive(-Math.abs(speed), -Math.abs(speed), 0.0)
    fun backwardRight(speed: Double) = this.drive(Math.abs(speed), -Math.abs(speed), 0.0)

    private fun clip(value: Double): Double = if(value > 0) Range.clip(value, 0.0, 1.0) else if(value < 0) Range.clip(value, -1.0, 0.0) else 0.0
}