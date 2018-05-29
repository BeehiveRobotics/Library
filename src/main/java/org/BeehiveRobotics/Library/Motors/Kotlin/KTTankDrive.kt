package org.BeehiveRobotics.Library.Motors.Kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.BeehiveRobotics.Library.Sensors.Kotlin.MRGyro
import org.BeehiveRobotics.Library.Util.BROpMode


class KTTankDrive(opMode: BROpMode, gearedType: GearedType) : Runnable {
    private val opMode: BROpMode = opMode
    private var gearedType: GearedType = gearedType
    private val hardwareMap: HardwareMap = opMode.hardwareMap
    private lateinit var FrontLeft: KTMotor
    private lateinit var FrontRight: KTMotor
    private lateinit var RearLeft: KTMotor
    private lateinit var RearRight: KTMotor
    lateinit var gyro: MRGyro
    private var CPR: Double = 0.toDouble() //Clicks per rotation of each motor
    private var RPM: Int = 0 //Rotations per Minute of each motor
    private var WD: Double = 0.toDouble()  //Wheel diameter
    private lateinit var model: MotorModel //Which model of motor is being used
    private var target: Double = 0.toDouble() //target for the motors to move to
    private var MIN_SPEED = 0.25
    private var MAX_SPEED = 1.0
    private var leftSpeed: Double = 0.toDouble()
    private var rightSpeed: Double = 0.toDouble()
    private var isBusy = false
    private val GYRO_LATENCY_OFFSET = 2.75
    private val GYRO_SLOW_MODE_OFFSET = 10
    private var heading: Int = 0

    fun mapHardware() {
        FrontLeft = KTMotor(opMode, "fl")
        FrontRight = KTMotor(opMode, "fr")
        RearLeft = KTMotor(opMode, "rl")
        RearRight = KTMotor(opMode, "rr")
        this.gyro = MRGyro(opMode, "g1")
    }
    fun setMinSpeed(speed: Double): KTTankDrive {
        this.MIN_SPEED = speed
        FrontLeft.setMinSpeed(MIN_SPEED)
        FrontRight.setMinSpeed(MIN_SPEED)
        RearLeft.setMinSpeed(MIN_SPEED)
        RearRight.setMinSpeed(MIN_SPEED)
        return this
    }

    fun setMaxSpeed(speed: Double): KTTankDrive {
        this.MAX_SPEED = speed
        FrontLeft.setMaxSpeed(MAX_SPEED)
        FrontRight.setMaxSpeed(MAX_SPEED)
        RearLeft.setMaxSpeed(MAX_SPEED)
        RearRight.setMaxSpeed(MAX_SPEED)
        return this
    }

    enum class GearedType {
        NORMAL, REVERSED
    }

    constructor(opMode: BROpMode) : this(opMode, GearedType.NORMAL)

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
        setModel(MotorModel.NEVEREST40)
        WD = 3.937
        setMinSpeed(MIN_SPEED)
    }

    private fun resetEncoders(): KTTankDrive {
        FrontLeft.resetEncoder()
        FrontRight.resetEncoder()
        RearLeft.resetEncoder()
        RearRight.resetEncoder()
        return this
    }

    private fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): KTTankDrive {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior)
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior)
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior)
        RearRight.setZeroPowerBehavior(zeroPowerBehavior)
        return this
    }

    private fun setRunMode(runMode: DcMotor.RunMode): KTTankDrive {
        FrontLeft.setRunMode(runMode)
        FrontRight.setRunMode(runMode)
        RearLeft.setRunMode(runMode)
        RearRight.setRunMode(runMode)
        return this
    }

    fun getGearedType(): GearedType {
        return gearedType
    }

    fun setGearedType(gearedType: GearedType): KTTankDrive {
        this.gearedType = gearedType
        return this
    }

    private fun getModel(): MotorModel {
        return model
    }

    private fun setModel(model: MotorModel): KTTankDrive {
        FrontLeft.setModel(model)
        FrontRight.setModel(model)
        RearLeft.setModel(model)
        RearRight.setModel(model)
        this.model = model
        this.RPM = model.RPM
        this.CPR = model.CPR
        return this
    }

    private fun setPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setPower(fl)
        FrontRight.setPower(fr)
        RearLeft.setPower(rl)
        RearRight.setPower(rr)
    }

    private fun setRawPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setRawPower(fl)
        FrontRight.setRawPower(fr)
        RearLeft.setRawPower(rl)
        RearRight.setRawPower(rr)
    }

    private fun setTarget(target: Double) {
        FrontLeft.setTarget(target)
        FrontRight.setTarget(target)
        RearLeft.setTarget(target)
        RearRight.setTarget(target)
        this.target = target
    }

    private fun inches_to_clicks(inches: Double): Double {
        val circumference = WD * Math.PI
        return CPR / circumference * inches
    }

    private fun drive(leftSpeed: Double, rightSpeed: Double, inches: Double) {
        this.drive(leftSpeed, rightSpeed, inches, true)
    }

    private fun drive(leftSpeed: Double, rightSpeed: Double, inches: Double, waitForCompletion: Boolean) {
        this.leftSpeed = leftSpeed
        this.rightSpeed = rightSpeed
        resetEncoders()
        val clicks = inches_to_clicks(inches)
        setTarget(clicks)
        if (waitForCompletion) {
            while (!(FrontLeft.isAtTarget() && FrontRight.isAtTarget() && RearLeft.isAtTarget() && RearRight.isAtTarget())) {
                setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed)
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    isBusy = false
                }
            }
        } else {
            val thread = Thread(this)
            thread.start()
        }
        stopMotors()
        isBusy = false
    }

    fun drive(leftSpeed: Double, rightSpeed: Double) {
        setRawPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed)
    }

    fun stopMotors() {
        FrontLeft.stopMotor()
        FrontRight.stopMotor()
        RearLeft.stopMotor()
        RearRight.stopMotor()
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

    override fun run() {
        val flThread = Thread(FrontLeft)
        val frThread = Thread(FrontRight)
        val rlThread = Thread(RearLeft)
        val rrThread = Thread(RearRight)
        flThread.start()
        frThread.start()
        rlThread.start()
        rrThread.start()
    }

    fun rightGyro(leftSpeed: Double, rightSpeed: Double, target: Double) {
        val adjustedTarget: Double = target + GYRO_LATENCY_OFFSET + GYRO_SLOW_MODE_OFFSET
        val finalTarget: Double = target + GYRO_LATENCY_OFFSET
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(leftSpeed, rightSpeed)
        var current: Int = heading
        var last: Int = heading
        while (current < target) {
            while (derivative <= 180 && opMode.opModeIsActive()) {
                derivative = current - last
                last = current
                current = gyro.getHeading()
            }
        }
        sleep(100)
        val start: Int = current - last
        val distance: Double = adjustedTarget - start
        var remaining: Double = distance
        var proportion: Double
        heading = start
        while (heading > adjustedTarget && opMode.opModeIsActive()) {
            heading = gyro.getHeading()
            remaining = (heading - start).toDouble()
            proportion = (1 - (Math.abs((remaining) / distance))) * 0.25 + 0.75
            drive(leftSpeed * proportion, rightSpeed * proportion)
        }
        val leftSpeed: Double = Math.min(0.2, leftSpeed)
        val rightSpeed: Double = Math.min(0.2, rightSpeed)
        while (heading > finalTarget && opMode.opModeIsActive()) {
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
        }
        stopMotors()
    }

    fun leftGyro(leftSpeed: Double, rightSpeed: Double, target: Double) {
        val adjustedTarget: Double = target - GYRO_LATENCY_OFFSET - GYRO_SLOW_MODE_OFFSET
        val finalTarget: Double = target - GYRO_LATENCY_OFFSET
        this.heading = gyro.getHeading()
        var derivative: Int = 0
        drive(leftSpeed, rightSpeed)
        var current: Int = heading
        var last: Int = heading
        while (current > target) {
            while (derivative >= -180 && opMode.opModeIsActive()) {
                derivative = current - last
                last = current
                current = gyro.getHeading()
            }
        }
        sleep(100)
        val start: Int = current - last
        val distance: Double = adjustedTarget - start
        var remaining: Double = distance
        var proportion: Double
        heading = start
        while (heading < adjustedTarget && opMode.opModeIsActive()) {
            heading = gyro.getHeading()
            remaining = (heading - start).toDouble()
            proportion = (1 - (Math.abs((remaining) / distance))) * 0.25 + 0.75
            drive(leftSpeed * proportion, rightSpeed * proportion)
        }
        val leftSpeed: Double = Math.min(0.2, leftSpeed)
        val rightSpeed: Double = Math.min(0.2, rightSpeed)
        while (heading < finalTarget && opMode.opModeIsActive()) {
            heading = gyro.getHeading()
            drive(leftSpeed, rightSpeed)
        }
        stopMotors()

    }

    fun sleep(miliseconds: Long) {
        try {
            Thread.sleep(miliseconds)
        } catch (e: Exception) {
        }
    }

}