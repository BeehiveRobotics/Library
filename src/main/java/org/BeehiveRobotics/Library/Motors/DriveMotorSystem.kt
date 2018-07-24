package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Sensors.REVIMU

abstract class DriveMotorSystem(protected val opMode: BROpMode, protected var gearedType: GearedType): Runnable {
    protected lateinit var FrontLeft: Motor
    protected lateinit var FrontRight: Motor
    protected lateinit var RearLeft: Motor
    protected lateinit var RearRight: Motor
    protected lateinit var Gyro: REVIMU
    protected var heading: Double = 0.0
        get() = Gyro.getHeading()
        private set
    protected val GYRO_LATENCY_OFFSET: Double = 2.75
    protected val GYRO_SLOW_MODE_OFFSET: Double = 10.0
    protected var CPR: Double = 1120.0
        private set
    protected var model: Motor.MotorModel = Motor.MotorModel.NEVEREST40
        protected set(model: Motor.MotorModel) {
            FrontLeft.model = model
            FrontRight.model = model
            RearLeft.model = model
            RearRight.model = model
            this.model = model
            this.CPR = model.CPR
    }
    protected var MIN_SPEED: Double = 0.25
        protected set(speed) {        
            this.MIN_SPEED = speed
            FrontLeft.MIN_SPEED = MIN_SPEED
            FrontRight.MIN_SPEED = MIN_SPEED
            RearLeft.MIN_SPEED = MIN_SPEED
            RearRight.MIN_SPEED = MIN_SPEED
        }
    protected var MAX_SPEED: Double = 1.0
        protected set(speed) {
            this.MAX_SPEED = speed
            FrontLeft.MAX_SPEED = MAX_SPEED
            FrontRight.MAX_SPEED = MAX_SPEED
            RearLeft.MAX_SPEED = MAX_SPEED
            RearRight.MAX_SPEED = MAX_SPEED
        }
    protected var WheelDiameter: Double = 3.937
    protected final val GYRO_FINAL_SPEED: Double = 0.2
    var isBusy: Boolean = true
    private var flSpeed: Double = 0.0
    private var frSpeed: Double = 0.0
    private var rlSpeed: Double = 0.0
    private var rrSpeed: Double = 0.0
    private var target: Double = 0.0
    private var inches: Double = 0.0
    private var task: Tasks = Tasks.Stop

    constructor(opMode: BROpMode): this(opMode, GearedType.NORMAL)

    enum class GearedType {
        NORMAL, REVERSE
    }

    protected enum class TurnDirection {
        LEFT, RIGHT
    }
    private enum class Tasks {
        EncoderDrive, RightGyro, LeftGyro, Stop
    }

    protected fun drive(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double, inches: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.flSpeed = flSpeed
        this.frSpeed = frSpeed
        this.rlSpeed = rlSpeed
        this.rrSpeed = rrSpeed
        this.inches = inches
        resetEncoders()
        val clicks: Double = inches_to_clicks(inches)
        val highestPower: Double = findHighestPower(listOf(flSpeed, frSpeed, rlSpeed, rrSpeed))
        val flTarget: Double = clicks * flSpeed /  highestPower
        val frTarget: Double = clicks * frSpeed / highestPower
        val rlTarget: Double = clicks * rlSpeed / highestPower
        val rrTarget: Double = clicks * rrSpeed / highestPower
        setTargets(flTarget, frTarget, rlTarget, rrTarget)
        if(waitForCompletion) {
            while(!allMotorsAtTarget()) {
                setPowers(flSpeed, frSpeed, rlSpeed, rrSpeed)
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
            }
            stopMotors()
            isBusy = false
        } else {
            this.task = Tasks.EncoderDrive
            val thread: Thread = Thread(this)
            thread.start()
        }
    } 


    protected fun rightGyro(flSpeed: Double, frSpeed: Double, rlSpeed: Double, rrSpeed: Double, target: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.flSpeed = flSpeed
        this.frSpeed = frSpeed
        this.rlSpeed = rlSpeed
        this.rrSpeed = rrSpeed
        this.target = target
        val adjustedTarget: Double = calculateAdjustedTarget(target, TurnDirection.RIGHT)
        val finalTarget: Double = calculateFinalTarget(target, TurnDirection.RIGHT)
        this.heading = Gyro.getHeading()
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
                    current = Gyro.getHeading()
                }
            }
            sleep(100)
            val start: Double = Gyro.getHeading()
            val distance: Double = adjustedTarget - start
            var proportion: Double
            heading = start
            while (heading > adjustedTarget) {
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = Gyro.getHeading()
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
                heading = Gyro.getHeading()
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
        this.heading = Gyro.getHeading()
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
                    current = Gyro.getHeading()
                }
            }
            sleep(100)
            val start: Double = Gyro.getHeading()
            val distance: Double = adjustedTarget - start
            var proportion: Double
            heading = start
            while (heading < adjustedTarget) {
                if(!opMode.opModeIsActive()) {
                    stopMotors()
                    return
                }
                heading = Gyro.getHeading()
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
                heading = Gyro.getHeading()
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

    fun mapHardware() {
        FrontLeft = Motor(opMode, "fl")
        FrontRight = Motor(opMode, "fr")
        RearLeft = Motor(opMode, "rl")
        RearRight = Motor(opMode, "rr")
        Gyro = REVIMU(opMode)
    }

    fun init() {
        if (gearedType == GearedType.NORMAL) {
            FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE)
            RearLeft.setDirection(DcMotorSimple.Direction.REVERSE)
            FrontRight.setDirection(DcMotorSimple.Direction.FORWARD)
            RearRight.setDirection(DcMotorSimple.Direction.FORWARD)
        } else {
            FrontRight.setDirection(DcMotorSimple.Direction.REVERSE)
            RearRight.setDirection(DcMotorSimple.Direction.REVERSE)
            FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD)
            RearLeft.setDirection(DcMotorSimple.Direction.FORWARD)
        }
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        resetEncoders()
        this.model = model
        Gyro.calibrate()
    }

    protected fun resetEncoders(): DriveMotorSystem {
        FrontLeft.resetEncoder()
        FrontRight.resetEncoder()
        RearLeft.resetEncoder()
        RearRight.resetEncoder()
        return this
    }

    fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): DriveMotorSystem {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior)
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior)
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior)
        RearRight.setZeroPowerBehavior(zeroPowerBehavior)
        return this
    }

    protected fun setRunMode(runMode: DcMotor.RunMode): DriveMotorSystem {
        FrontLeft.runMode = runMode
        FrontRight.runMode = runMode
        RearLeft.runMode = runMode
        RearRight.runMode = runMode
        return this
    }

    protected fun setGearedType(gearedType: GearedType): DriveMotorSystem {
        this.gearedType = gearedType
        return this
    }

    protected fun allMotorsAtTarget(): Boolean {
        return FrontLeft.isAtTarget() && FrontRight.isAtTarget() && RearLeft.isAtTarget() && RearRight.isAtTarget()
    }

    protected fun setPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setPower(fl)
        FrontRight.setPower(fr)
        RearLeft.setPower(rl)
        RearRight.setPower(rr)
    }

    protected fun setRawPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setRawPower(fl)
        FrontRight.setRawPower(fr)
        RearLeft.setRawPower(rl)
        RearRight.setRawPower(rr)
    }

    internal fun setTargets(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.target = fl
        FrontRight.target = fr
        RearLeft.target = rl
        RearRight.target = rr
    }

    internal fun inches_to_clicks(inches: Double): Double {
        val circumference = WheelDiameter * Math.PI
        return CPR / circumference * inches

    }

    fun stopMotors(waitForCompletion: Boolean = true) {
        if(waitForCompletion) {
            FrontLeft.stopMotor()
            FrontRight.stopMotor()
            RearLeft.stopMotor()
            RearRight.stopMotor()
        } else {
            this.task = Tasks.Stop
            val thread: Thread = Thread(this)
            thread.start()
        }
    }

    fun sleep(milliseconds: Long) {
        val time: ElapsedTime = ElapsedTime()
        time.reset()
        while(time.milliseconds() < milliseconds) {
            if(!(opMode.opModeIsActive())) {
                return
            }
        }

    }

    fun avgSpeed() = (Math.abs(FrontLeft.power) + Math.abs(FrontRight.power) + Math.abs(RearLeft.power) + Math.abs(RearRight.power)) / 4

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
        val proportion = (1 - (Math.abs((remaining) / distance))) * 0.75 + 0.25
        return proportion
    }

    private fun findHighestPower(powers: List<Double>): Double {
        var high: Double = Double.MIN_VALUE
        for (value in powers) {
            if(Math.abs(value) > high){
                high = Math.abs(value)
            }
        }
        return high
    }
    override fun run() {
        isBusy = true
        when(task) {
            Tasks.EncoderDrive -> drive(flSpeed, frSpeed, rlSpeed, rrSpeed, inches, true)
            Tasks.RightGyro -> rightGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, true)
            Tasks.LeftGyro -> leftGyro(flSpeed, frSpeed, rlSpeed, rrSpeed, target, true)
            Tasks.Stop -> stopMotors()
        }
        isBusy = false
    }
} 