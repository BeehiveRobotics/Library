package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Motors.Motor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Sensors.REVIMU

abstract class DriveMotorSystem(protected val opMode: BROpMode, protected var gearedType: GearedType = GearedType.NORMAL, private val gearRatio: Double = 1.0): RobotSystem(opMode), Runnable {
    protected val frontLeft = Motor(opMode, "fl")
    protected val frontRight = Motor(opMode, "fr")
    protected val rearLeft = Motor(opMode, "rl")
    protected val rearRight = Motor(opMode, "rr")
    protected val gyro = REVIMU(opMode)
    protected var heading = 0.0
        get() = gyro.heading
        private set
    protected val GYRO_LATENCY_OFFSET = 2.75
    protected val GYRO_SLOW_MODE_OFFSET = 10.0
    var model = Motor.MotorModel.NEVEREST40
        set(model) {
            frontLeft.model = model
            frontRight.model = model
            rearLeft.model = model
            rearRight.model = model
            field = model
        }
    protected var MIN_SPEED: Double = 0.25
        protected set(speed) {        
            frontLeft.MIN_SPEED = MIN_SPEED
            frontRight.MIN_SPEED = MIN_SPEED
            rearLeft.MIN_SPEED = MIN_SPEED
            rearRight.MIN_SPEED = MIN_SPEED
            field = speed
        }
    protected var MAX_SPEED: Double = 1.0
        protected set(value) {
            frontLeft.MAX_SPEED = value
            frontRight.MAX_SPEED = value
            rearLeft.MAX_SPEED = value
            rearRight.MAX_SPEED = value
            field = value
        }
    protected var MAX_NO_SLIP_SPEED = 1.0
        protected set(value) {
            frontLeft.MAX_NO_SLIP_SPEED = value
            frontRight.MAX_NO_SLIP_SPEED = value
            rearLeft.MAX_NO_SLIP_SPEED = value
            rearRight.MAX_NO_SLIP_SPEED = value
            field = value
        }
    protected var RAMPING_COEFFICIENT: Double = 0.8
        protected set(value) {
            frontLeft.RAMPING_COEFFICIENT = value
            frontRight.RAMPING_COEFFICIENT = value
            rearLeft.RAMPING_COEFFICIENT = value
            rearRight.RAMPING_COEFFICIENT = value
            field = value
        }
    protected var RAMP_CLICKS_PROPORTION: Double = 1.0
        protected set(value) {
            frontLeft.RAMP_CLICKS_PROPORTION = value
            frontRight.RAMP_CLICKS_PROPORTION = value
            rearLeft.RAMP_CLICKS_PROPORTION = value
            rearRight.RAMP_CLICKS_PROPORTION = value
            field = value
        }
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        set(zeroPowerBehavior) {
            this.frontLeft.zeroPowerBehavior = zeroPowerBehavior
            this.frontRight.zeroPowerBehavior = zeroPowerBehavior
            this.rearLeft.zeroPowerBehavior = zeroPowerBehavior
            this.rearRight.zeroPowerBehavior = zeroPowerBehavior
            field = zeroPowerBehavior
        }
        get() = this.zeroPowerBehavior
    protected var runMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set (runMode) {
            frontLeft.runMode = runMode
            frontRight.runMode = runMode
            rearLeft.runMode = runMode
            rearRight.runMode = runMode
            field = runMode
        }
        get() = this.runMode

    protected var WHEEL_DIAMETER: Double = 3.937
    protected final val GYRO_FINAL_SPEED: Double = 0.2
    private var flSpeed: Double = 0.0
    private var frSpeed: Double = 0.0
    private var rlSpeed: Double = 0.0
    private var rrSpeed: Double = 0.0
    private var target: Double = 0.0
    private var inches: Double = 0.0
    private var task: Tasks = Tasks.Stop

    enum class GearedType {
        NORMAL, REVERSE
    }

    protected enum class TurnDirection {
        LEFT, RIGHT
    }
    private enum class Tasks {
        EncoderDrive, RightGyro, LeftGyro, Stop
    }

    override fun init() {
        if (gearedType == GearedType.REVERSE) {
            frontLeft.direction = DcMotorSimple.Direction.REVERSE
            rearLeft.direction = DcMotorSimple.Direction.REVERSE
            frontRight.direction = DcMotorSimple.Direction.FORWARD
            rearRight.direction = DcMotorSimple.Direction.FORWARD
        } else {
            frontRight.direction = DcMotorSimple.Direction.REVERSE
            rearRight.direction = DcMotorSimple.Direction.REVERSE
            frontLeft.direction = DcMotorSimple.Direction.FORWARD
            rearLeft.direction = DcMotorSimple.Direction.FORWARD
        }
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        this.runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        resetEncoders()
        this.model = model
        if(opMode.opModeType == BROpMode.OpModeType.Autonomous) gyro.calibrate()
    }

    protected fun drive(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double, inches: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.flSpeed = flSpeed
        this.frSpeed = frSpeed
        this.rlSpeed = rlSpeed
        this.rrSpeed = rrSpeed
        this.inches = inches
        //this.RAMPING_COEFFICIENT = -0.015*inches + 1.5
        resetEncoders()
        val clicks: Double = inches_to_clicks(inches)
        val highestPower: Double = Math.max(listOf(Math.abs(flSpeed), Math.abs(frSpeed), Math.abs(rlSpeed), Math.abs(rrSpeed)).max() ?: java.lang.Double.MIN_VALUE, java.lang.Double.MIN_VALUE)
        val flTarget: Double = clicks * flSpeed / highestPower
        val frTarget: Double = clicks * frSpeed / highestPower
        val rlTarget: Double = clicks * rlSpeed / highestPower
        val rrTarget: Double = clicks * rrSpeed / highestPower
        setTargets(flTarget, frTarget, rlTarget, rrTarget)
        //setContantRampPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
        if(waitForCompletion) {
            while(!allMotorsAtTarget()) {
                setPiecewiseRampPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                opMode.dashboard.showLine(toString()) //Enable this line for information on each of the motors
            }
            stopMotors()
            isBusy = false
        } else {
            this.task = Tasks.EncoderDrive
            val thread: Thread = Thread(this)
            thread.start()
        }
    } 

    fun drive(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double) = setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
    fun drive(leftSpeed: Double, rightSpeed: Double) = setRawPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed)

    protected fun rightGyro(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double, target: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.flSpeed = flSpeed
        this.frSpeed = frSpeed
        this.rlSpeed = rlSpeed
        this.rrSpeed = rrSpeed
        this.target = target
        val adjustedTarget: Double = calculateAdjustedTarget(target, TurnDirection.RIGHT)
        val finalTarget: Double = calculateFinalTarget(target, TurnDirection.RIGHT)
        this.heading = gyro.heading
        var derivative: Double = 0.0
        if(waitForCompletion) {
            setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
            var current: Double = heading
            var last: Double = heading
            while(current < target) {
                while(derivative <= 180) {
                    if(!opMode.opModeIsActive()) {
                        stopMotors()
                        return
                    }
                    derivative = current - last
                    last = current
                    current = gyro.heading
                }
            }
            sleep(100)
            val start: Double = gyro.heading
            val distance: Double = adjustedTarget - start
            var proportion: Double
            heading = start
            while (heading > adjustedTarget) {
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = gyro.heading
                proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
                setRawPowers(flSpeed * proportion, frSpeed * proportion, rlSpeed * proportion, rrSpeed * proportion)
            }
            val flSpeed: Double = Math.min(GYRO_FINAL_SPEED, flSpeed)
            val frSpeed: Double = Math.min(GYRO_FINAL_SPEED, frSpeed)
            val rlSpeed: Double = Math.min(GYRO_FINAL_SPEED, rlSpeed)
            val rrSpeed: Double = Math.min(GYRO_FINAL_SPEED, rrSpeed)
            while (heading > finalTarget) {
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = gyro.heading
                setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
            }
            stopMotors()
            isBusy = false
            

        } else {
            this.task = Tasks.RightGyro
            val thread: Thread = Thread(this)
            thread.start()
        }
    }

    protected fun leftGyro(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double, target: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.flSpeed = flSpeed
        this.frSpeed = frSpeed
        this.rlSpeed = rlSpeed
        this.rrSpeed = rrSpeed
        this.target = target
        val adjustedTarget: Double = calculateAdjustedTarget(target, TurnDirection.RIGHT)
        val finalTarget: Double = calculateFinalTarget(target, TurnDirection.RIGHT)
        this.heading = gyro.heading
        var derivative: Double = 0.0
        if(waitForCompletion) {
            setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
            var current: Double = heading
            var last: Double = heading
            while(current > target) {
                while(derivative >= -180) {
                    if(!opMode.opModeIsActive()) {
                        stopMotors()
                        return
                    }
                    derivative = current - last
                    last = current
                    current = gyro.heading
                }
            }
            sleep(100)
            val start: Double = gyro.heading
            val distance: Double = adjustedTarget - start
            var proportion: Double
            heading = start
            while (heading < adjustedTarget) {
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = gyro.heading
                proportion = calculateProportion(heading.toDouble(), start.toDouble(), distance)
                setRawPowers(flSpeed * proportion, frSpeed * proportion, rlSpeed * proportion, rrSpeed * proportion)
            }
            val flSpeed: Double = Math.min(GYRO_FINAL_SPEED, flSpeed)
            val frSpeed: Double = Math.min(GYRO_FINAL_SPEED, frSpeed)
            val rlSpeed: Double = Math.min(GYRO_FINAL_SPEED, rlSpeed)
            val rrSpeed: Double = Math.min(GYRO_FINAL_SPEED, rrSpeed)
            while (heading < finalTarget) {
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = gyro.heading
                setRawPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
            }
            stopMotors()
            isBusy = false
        } else {
            this.task = Tasks.LeftGyro
            val thread: Thread = Thread(this)
            thread.start()
        }
    }

    protected fun resetEncoders(): DriveMotorSystem {
        frontLeft.resetEncoder()
        frontRight.resetEncoder()
        rearLeft.resetEncoder()
        rearRight.resetEncoder()
        return this
    }


    protected fun setGearedType(gearedType: GearedType): DriveMotorSystem {
        this.gearedType = gearedType
        return this
    }
    
    protected fun allMotorsAtTarget(): Boolean = frontLeft.isAtTarget() && frontRight.isAtTarget() && rearLeft.isAtTarget() && rearRight.isAtTarget()

    protected fun setContantRampPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        frontLeft.power = fl
        frontRight.power = fr
        rearLeft.power = rl
        rearRight.power = rr
    }

    protected fun setPiecewiseRampPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        frontLeft.power = fl
        frontRight.power = fr
        rearLeft.power = rl
        rearRight.power = rr
    }

    protected fun setRawPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        frontLeft.motor.power = fl
        frontRight.motor.power = fr
        rearLeft.motor.power = rl
        rearRight.motor.power = rr
    }

    internal fun setTargets(fl: Double, fr: Double, rl: Double, rr: Double) {
        frontLeft.target = fl
        frontRight.target = fr
        rearLeft.target = rl
        rearRight.target = rr
    }

    fun setRampingTypes(all: Motor.RampingType) {
        frontLeft.rampingType = all
        frontRight.rampingType = all
        rearLeft.rampingType = all
        rearRight.rampingType = all

    }

    internal fun inches_to_clicks(inches: Double): Double {
        val circumference = WHEEL_DIAMETER * Math.PI
        return model.CPR / circumference * inches / gearRatio

    }

    fun stopMotors(waitForCompletion: Boolean = true) {
        if(waitForCompletion) {
            frontLeft.stopMotor()
            frontRight.stopMotor()
            rearLeft.stopMotor()
            rearRight.stopMotor()
        } else {
            this.task = Tasks.Stop
            val thread: Thread = Thread(this)
            thread.start()
        }
    }

    fun avgSpeed() = (Math.abs(frontLeft.targetPower) + Math.abs(frontRight.targetPower) + Math.abs(rearLeft.targetPower) + Math.abs(rearRight.targetPower)) / 4

    protected fun calculateAdjustedTarget(target: Double, direction: TurnDirection): Double {
        when(direction) {
            TurnDirection.LEFT -> return target - GYRO_LATENCY_OFFSET - GYRO_SLOW_MODE_OFFSET
            TurnDirection.RIGHT -> return target + GYRO_LATENCY_OFFSET + GYRO_SLOW_MODE_OFFSET
        }
    }

    protected fun calculateFinalTarget(target: Double, direction: TurnDirection): Double {
        when(direction) {
            TurnDirection.LEFT -> return target - GYRO_LATENCY_OFFSET
            TurnDirection.RIGHT -> return target + GYRO_LATENCY_OFFSET
        }
    }

    protected fun calculateProportion(current: Double, start: Double, distance: Double): Double {
        val remaining = current - start
        val proportion = (1 - (Math.abs((remaining) / distance))) * (1-MIN_SPEED) + MIN_SPEED
        return proportion
    }

    override fun run() {
        isBusy = true
        //opMode.dashboard.addLine("Multi-thread ID: ${Thread.currentThread().id}") //Use  this line to display the thread id of drivemotorsystem multi-threading
        when(task) {
            Tasks.EncoderDrive -> drive(flSpeed, frSpeed, rlSpeed, rrSpeed, inches, true)
            Tasks.RightGyro -> rightGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, true)
            Tasks.LeftGyro -> leftGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, true)
            Tasks.Stop -> stopMotors()
        }
        isBusy = false
    }

    override fun toString(): String =
        "FrontLeft: \n" + 
        "\tTarget Power: ${frontLeft.targetPower}\n" +
        "\tCurrent Power: ${frontLeft.power}\n" + 
        "\tTarget Clicks: ${frontLeft.target}\n" + 
        "\tCurrent Clicks: ${frontLeft.currentPosition}\n" + 
        "FrontRight: \n" +
        "\tTarget Power: ${frontRight.targetPower}\n" +
        "\tCurrent Power: ${frontRight.power}\n" + 
        "\tTarget Clicks: ${frontRight.target}\n" + 
        "\tCurrent Clicks: ${frontRight.currentPosition}\n" + 
        "RearLeft: \n" + 
        "\tTarget Power: ${rearLeft.targetPower}\n" +
        "\tCurrent Power: ${rearLeft.power}\n" + 
        "\tTarget Clicks: ${rearLeft.target}\n" + 
        "\tCurrent Clicks: ${rearLeft.currentPosition}\n" + 
        "RearRight: \n" + 
        "\tTarget Power: ${rearRight.targetPower}\n" +
        "\tCurrent Power: ${rearRight.power}\n" + 
        "\tTarget Clicks: ${rearRight.target}\n" + 
        "\tCurrent Clicks: ${rearRight.currentPosition}\n" 
} 