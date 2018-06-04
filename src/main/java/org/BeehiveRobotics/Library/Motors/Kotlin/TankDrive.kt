package org.BeehiveRobotics.Library.Motors.Kotlin

import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

@Suppress("NAME_SHADOWING")
class TankDrive(opMode: BROpMode, gearedType: KTDriveMotorSystem.GearedType = GearedType.NORMAL) : Runnable, KTDriveMotorSystem(opMode, gearedType) {
    enum class DriveState {
        Encoders, Gyro, Time, Other, Stopped
    }
    private var currentDriveMode: DriveState = DriveState.Stopped
    fun drive(left: Double, right: Double, inches: Double, waitForCompletion: Boolean = true) {
        currentDriveMode = DriveState.Encoders
        super.resetEncoders()
        val clicks: Double = super.inches_to_clicks(inches)
        super.setTargets(clicks, clicks, clicks, clicks)
        if (waitForCompletion) {
            while (!(super.FrontLeft.isAtTarget() && super.FrontRight.isAtTarget() && super.RearLeft.isAtTarget() && super.RearRight.isAtTarget())) {
                setPowers(left, right, left, right)
                if (!opMode.opModeIsActive()) {
                    super.stopMotors()
                    isBusy = false
                }
            }
        } else {
            val thread: Thread = Thread(this)
            thread.start()
        }
        super.stopMotors()
        isBusy = false
        currentDriveMode = DriveState.Stopped
    }

    fun drive(left: Double, right: Double) {
        setRawPowers(left, right, left, right)
    }

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

    fun rightGyro(leftSpeed: Double, rightSpeed: Double, target: Double) {
        currentDriveMode = DriveState.Gyro
        val adjustedTarget: Double = calculateAdjustedTarget(target, TurnDirection.RIGHT)
        val finalTarget: Double = calculateFinalTarget(target, TurnDirection.RIGHT)
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(leftSpeed, rightSpeed)
        var current: Int = heading
        var last: Int = heading
        while (current < target) {
            while (derivative <= 180) {
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                derivative = current - last
                last = current
                current = gyro.getHeading()
            }
        }
        sleep(100)
        val start: Int = gyro.getHeading()
        val distance: Double = adjustedTarget - start
        var remaining: Double = distance
        var proportion: Double
        heading = start
        while (heading > adjustedTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
            drive(leftSpeed * proportion, rightSpeed * proportion)
        }
        val leftSpeed: Double = Math.min(GYRO_FINAL_SPEED, leftSpeed)
        val rightSpeed: Double = Math.min(GYRO_FINAL_SPEED, rightSpeed)
        while (heading > finalTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
        }
        stopMotors()
        currentDriveMode = DriveState.Stopped
    }

    fun rightGyro(speed: Double, target: Double) {
        rightGyro(Math.abs(speed), -Math.abs(speed), target)
    }

    fun leftGyro(leftSpeed: Double, rightSpeed: Double, target: Double) {
        currentDriveMode = DriveState.Gyro
        val adjustedTarget: Double = calculateAdjustedTarget(target, TurnDirection.LEFT)
        val finalTarget: Double = calculateFinalTarget(target, TurnDirection.LEFT)
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(leftSpeed, rightSpeed)
        var current: Int = heading
        var last: Int = heading
        while (current > target) {
            while (derivative >= -180) {
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                derivative = current - last
                last = current
                current = gyro.getHeading()
            }
        }
        sleep(100)
        val start: Int = gyro.getHeading()
        val distance: Double = adjustedTarget - start
        var remaining: Double = distance
        var proportion: Double
        heading = start
        while (heading < adjustedTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
            drive(leftSpeed * proportion, rightSpeed * proportion)
        }
        val leftSpeed: Double = Math.min(GYRO_FINAL_SPEED, leftSpeed)
        val rightSpeed: Double = Math.min(GYRO_FINAL_SPEED, rightSpeed)
        while (heading < finalTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
        }
        stopMotors()
        currentDriveMode = DriveState.Stopped
    }
    fun leftGyro(speed: Double, target: Double) {
        leftGyro(-Math.abs(speed), Math.abs(speed), target)
    }
}
//TODO: Add actual DriveState stuff into gyro turning to allow for gyro turning to be on different thread.