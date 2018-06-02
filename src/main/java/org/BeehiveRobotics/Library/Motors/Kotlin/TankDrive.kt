package org.BeehiveRobotics.Library.Motors.Kotlin

import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode

@Suppress("NAME_SHADOWING")
class TankDrive(opMode: BROpMode, gearedType: KTDriveMotorSystem.GearedType) : Runnable, KTDriveMotorSystem(opMode, gearedType) {
    fun drive(left: Double, right: Double, inches: Double, waitForCompletion: Boolean = true) {
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
    }

    fun drive(left: Double, right: Double) {
        setRawPowers(left, right, left, right)
    }

    fun forward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, speed, inches)
    }

    fun backward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, speed, inches)
    }

    fun spinRight(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, -speed, inches)
    }

    fun spinLeft(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, -speed, inches)
    }

    fun leftForward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(speed, 0.0, inches)
    }

    fun leftBackward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(speed, 0.0, inches)
    }

    fun rightForward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = Math.abs(speed)
        drive(0.0, speed, inches)
    }

    fun rightBackward(speed: Double, inches: Double) {
        var speed: Double = speed
        speed = -Math.abs(speed)
        drive(0.0, speed, inches)
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
                    return
                }
                derivative = current - last
                last = current
                current = gyro.getHeading()
                //opMode.telemetry.addData("AVG Speed", avgSpeed())
                //opMode.telemetry.update()
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
                return
            }
            heading = gyro.getHeading()
            //remaining = (heading - start).toDouble()
            proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
            drive(leftSpeed * proportion, rightSpeed * proportion)
            //opMode.telemetry.addData("AVG Speed", avgSpeed())
            //opMode.telemetry.update()
        }
        val leftSpeed: Double = GYRO_FINAL_SPEED
        val rightSpeed: Double = GYRO_FINAL_SPEED
        while (heading > finalTarget) {
            if (!opMode.opModeIsActive()) {
                return
            }
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
            //opMode.telemetry.addData("AVG Speed", avgSpeed())
            //opMode.telemetry.update()
        }
        stopMotors()
    }

    fun leftGyro(leftSpeed: Double, rightSpeed: Double, target: Double) {
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
                    return
                }
                derivative = current - last
                last = current
                current = gyro.getHeading()
                //opMode.telemetry.addData("AVG Speed", avgSpeed())
                //opMode.telemetry.update()
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
                return
            }
            heading = gyro.getHeading()
            //remaining = (heading - start).toDouble()
            proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
            drive(leftSpeed * proportion, rightSpeed * proportion)
            //opMode.telemetry.addData("AVG Speed", avgSpeed())
            //opMode.telemetry.update()
        }
        val leftSpeed: Double = GYRO_FINAL_SPEED
        val rightSpeed: Double = GYRO_FINAL_SPEED
        while (heading < finalTarget) {
            if (!opMode.opModeIsActive()) {
                return
            }
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
            //opMode.telemetry.addData("AVG Speed", avgSpeed())
            //opMode.telemetry.update()
        }
        stopMotors()
    }

}