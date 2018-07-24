package org.BeehiveRobotics.Library.Motors

import org.BeehiveRobotics.Library.Util.BROpMode

@Suppress("NAME_SHADOWING")
class TankDrive(opMode: BROpMode, gearedType: DriveMotorSystem.GearedType = GearedType.NORMAL) : Runnable, DriveMotorSystem(opMode, gearedType) {

    fun drive(left: Double, right: Double, inches: Double, waitForCompletion: Boolean = true) = super.drive(left, right, left, right, inches, waitForCompletion)
    fun drive(left: Double, right: Double) = setRawPowers(left, right, left, right)

    fun rightGyro(leftSpeed: Double, rightSpeed: Double, target: Double, waitForCompletion: Boolean = true) = super.rightGyro(leftSpeed, rightSpeed, leftSpeed, rightSpeed, target, waitForCompletion)
    fun rightGyro(speed: Double, target: Double) = rightGyro(Math.abs(speed), -Math.abs(speed), target)

    fun leftGyro(leftSpeed: Double, rightSpeed: Double, target: Double, waitForCompletion: Boolean = true) = super.leftGyro(leftSpeed, rightSpeed, leftSpeed, rightSpeed, target, waitForCompletion)
    fun leftGyro(speed: Double, target: Double) = leftGyro(-Math.abs(speed), Math.abs(speed), target)

    fun forward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, speed, inches, waitForCompletion)
    }

    fun backward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, speed, inches, waitForCompletion)
    }

    fun spinRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, -speed, inches, waitForCompletion)
    }

    fun spinLeft(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, -speed, inches, waitForCompletion)
    }

    fun leftForward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, 0.0, inches, waitForCompletion)
    }

    fun leftBackward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, 0.0, inches, waitForCompletion)
    }

    fun rightForward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(0.0, speed, inches, waitForCompletion)
    }

    fun rightBackward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(0.0, speed, inches, waitForCompletion)
    }

    fun forward(speed: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, speed)
    }

    fun backward(speed: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, speed)
    }

    fun spinRight(speed: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, -speed)
    }

    fun spinLeft(speed: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, -speed)
    }

    fun leftForward(speed: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, 0.0)
    }

    fun leftBackward(speed: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, 0.0)
    }

    fun rightForward(speed: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(0.0, speed)
    }

    fun rightBackward(speed: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(0.0, speed)
    }
}