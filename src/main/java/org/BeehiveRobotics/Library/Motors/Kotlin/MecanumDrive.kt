package org.BeehiveRobotics.Library.Motors.Kotlin

import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode
import com.qualcomm.robotcore.util.Range

@Suppress("NAME_SHADOWING")

class MecanumDrive(opMode: BROpMode, gearedType: KTDriveMotorSystem.GearedType = GearedType.NORMAL): Runnable, KTDriveMotorSystem(opMode, gearedType) {
    fun drive(x: Double, y: Double, z: Double, inches: Double, waitForCompletion: Boolean = true) {
        super.resetEncoders()
        val clicks: Double = super.inches_to_clicks(inches)
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        val list: DoubleArray = DoubleArray(4)
        list[0] = flSpeed; list[1] = frSpeed; list[2] = rlSpeed; list[3] = rrSpeed
        val high: Double = findHigh(list)
        val flTarget: Double = clicks * flSpeed / high
        val frTarget: Double = clicks * frSpeed / high
        val rlTarget: Double = clicks * rlSpeed / high
        val rrTarget: Double = clicks * rrSpeed / high
        super.setTargets(flTarget, frTarget, rlTarget, rrTarget)
        if(waitForCompletion) {
            while (!(super.FrontLeft.isAtTarget() && super.FrontRight.isAtTarget() && super.RearLeft.isAtTarget() && super.RearRight.isAtTarget())) {
                super.setPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
                if (!opMode.opModeIsActive()) {
                    super.stopMotors()
                    isBusy = false
                    return
                }
            }
        } else {
            val thread: Thread = Thread(this)
            thread.start()
        }
        super.stopMotors()
        isBusy = false
    }
    fun drive(x: Double, y: Double, z: Double) {
        val flSpeed: Double = clip(y + z + x)
        val frSpeed: Double = clip(y - z - x)
        val rlSpeed: Double = clip(y + z - x)
        val rrSpeed: Double = clip(y - z + x)
        super.setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
    }
    fun drive(xLeft: Double, yLeft: Double, xRight: Double, yRight: Double) {
        val flSpeed: Double = clip(yLeft - xLeft)
        val frSpeed: Double = clip(yRight + xRight)
        val rlSpeed: Double = clip(yLeft + xLeft)
        val rrSpeed: Double = clip(yRight - xRight)
        super.setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
    }


    fun forward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, Math.abs(speed), 0.0, inches, waitForCompletion)
    }
    fun backward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, -Math.abs(speed), 0.0, inches, waitForCompletion)
    }
    fun strafeLeft(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(-Math.abs(speed), 0.0, 0.0, inches, waitForCompletion)
    }
    fun strafeRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(Math.abs(speed), 0.0, 0.0, inches, waitForCompletion)
    }
    fun spinLeft(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, 0.0, -Math.abs(speed), inches, waitForCompletion)
    }
    fun spinRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, 0.0, Math.abs(speed), inches, waitForCompletion)
    }
    fun leftForward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, 0.5*Math.abs(speed), 0.5*Math.abs(speed), inches, waitForCompletion)
    }
    fun leftBackward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, -0.5*Math.abs(speed), -0.5*Math.abs(speed), inches, waitForCompletion)
    }
    fun rightForward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, 0.5*Math.abs(speed), -0.5*Math.abs(speed), inches, waitForCompletion)
    }
    fun rightBackward(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(0.0, -0.5*Math.abs(speed), 0.5*Math.abs(speed), inches, waitForCompletion)
    }
    fun forwardLeft(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(-Math.abs(speed), Math.abs(speed), 0.0, inches, waitForCompletion)
    }
    fun forwardRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(Math.abs(speed), Math.abs(speed), 0.0, inches, waitForCompletion)
    }
    fun backwardLeft(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(-Math.abs(speed), -Math.abs(speed), 0.0, inches, waitForCompletion)
    }
    fun backwardRight(speed: Double, inches: Double, waitForCompletion: Boolean = true) {
        drive(Math.abs(speed), -Math.abs(speed), 0.0, inches, waitForCompletion)
    }

    fun forward(speed: Double) {
        drive(0.0, Math.abs(speed), 0.0)
    }
    fun backward(speed: Double) {
        drive(0.0, -Math.abs(speed), 0.0)
    }
    fun strafeLeft(speed: Double) {
        drive(-Math.abs(speed), 0.0, 0.0)
    }
    fun strafeRight(speed: Double) {
        drive(Math.abs(speed), 0.0, 0.0)
    }
    fun spinLeft(speed: Double) {
        drive(0.0, 0.0, -Math.abs(speed))
    }
    fun spinRight(speed: Double) {
        drive(0.0, 0.0, Math.abs(speed))
    }
    fun leftForward(speed: Double) {
        drive(0.0, 0.5*Math.abs(speed), 0.5*Math.abs(speed))
    }
    fun leftBackward(speed: Double) {
        drive(0.0, -0.5*Math.abs(speed), -0.5*Math.abs(speed))
    }
    fun rightForward(speed: Double) {
        drive(0.0, 0.5*Math.abs(speed), -0.5*Math.abs(speed))
    }
    fun rightBackward(speed: Double) {
        drive(0.0, -0.5*Math.abs(speed), 0.5*Math.abs(speed))
    }
    fun forwardLeft(speed: Double) {
        drive(-Math.abs(speed), Math.abs(speed), 0.0)
    }
    fun forwardRight(speed: Double) {
        drive(Math.abs(speed), Math.abs(speed), 0.0)
    }
    fun backwardLeft(speed: Double) {
        drive(-Math.abs(speed), -Math.abs(speed), 0.0)
    }
    fun backwardRight(speed: Double) {
        drive(Math.abs(speed), -Math.abs(speed), 0.0)
    }

    fun rightGyro(x: Double, y: Double, z: Double, target: Double) {
        val adjustedTarget: Double = super.calculateAdjustedTarget(target, TurnDirection.RIGHT)
        val finalTarget: Double = super.calculateFinalTarget(target, TurnDirection.RIGHT)
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(x, y, z)
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
            drive(x * proportion, y * proportion, z * proportion)
        }
        val x: Double = Math.min(GYRO_FINAL_SPEED, x)
        val y: Double = Math.min(GYRO_FINAL_SPEED, y)
        val z: Double = Math.min(GYRO_FINAL_SPEED, z)
        while (heading > finalTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            drive(x, y, z)
        }
        stopMotors()
    }

    fun leftGyro(x: Double, y: Double, z: Double, target: Double) {
        val adjustedTarget: Double = super.calculateAdjustedTarget(target, TurnDirection.LEFT)
        val finalTarget: Double = super.calculateFinalTarget(target, TurnDirection.LEFT)
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(x, y, z)
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
            drive(x * proportion, y * proportion, z * proportion)
        }
        val x: Double = Math.min(GYRO_FINAL_SPEED, x)
        val y: Double = Math.min(GYRO_FINAL_SPEED, y)
        val z: Double = Math.min(GYRO_FINAL_SPEED, z)
        while (heading < finalTarget) {
            if (!opMode.opModeIsActive()) {
                stopMotors()
                return
            }
            heading = gyro.getHeading()
            drive(x, y, z)
        }
        stopMotors()
    }


    private fun findHigh(values: DoubleArray): Double {
        var high: Double = Double.MIN_VALUE
        for (value: Double in values) {
            if(Math.abs(value) > high){
                high = Math.abs(value)
            }
        }
        return high
    }
    private fun clip(value: Double): Double {
        if(value > 0) {
            return Range.clip(value, 0.0, 1.0)
        } else if(value < 0) {
            return Range.clip(value, -1.0, 0.0)
        } else return 0.0
    }
}
//TODO: Add DriveState stuff to allow for gyro turning to run on another thread.