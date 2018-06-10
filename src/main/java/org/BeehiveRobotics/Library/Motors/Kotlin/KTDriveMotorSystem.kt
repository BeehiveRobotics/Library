package org.BeehiveRobotics.Library.Motors.Kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.BeehiveRobotics.Library.Sensors.Kotlin.MRGyro
import org.BeehiveRobotics.Library.Util.Kotlin.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime

abstract class KTDriveMotorSystem(opMode: BROpMode, gearedType: GearedType) : Runnable {
    protected val opMode: BROpMode = opMode
    protected lateinit var FrontLeft: Motor
    protected lateinit var FrontRight: Motor
    protected lateinit var RearLeft: Motor
    protected lateinit var RearRight: Motor
    protected lateinit var gyro: MRGyro
    protected var heading: Int = 0
    protected val GYRO_LATENCY_OFFSET: Double = 2.75
    protected val GYRO_SLOW_MODE_OFFSET: Double = 10.0
    protected var CPR: Double = 1120.0
    protected var WheelDiameter: Double = 0.0
    protected lateinit var model: MotorModel
    protected var MIN_SPEED: Double = 0.25
    protected var MAX_SPEED: Double = 1.0
    protected final val GYRO_FINAL_SPEED: Double = 0.2
    protected var isBusy = false
    protected var gearedType: GearedType = gearedType

    constructor(opMode: BROpMode) : this(opMode, GearedType.NORMAL)

    enum class GearedType {
        NORMAL, REVERSE
    }

    enum class TurnDirection {
        LEFT, RIGHT
    }

    fun mapHardware() {
        FrontLeft = Motor(opMode, "fl")
        FrontRight = Motor(opMode, "fr")
        RearLeft = Motor(opMode, "rl")
        RearRight = Motor(opMode, "rr")
    }

    fun setMinSpeed(speed: Double): KTDriveMotorSystem {
        this.MIN_SPEED = speed
        FrontLeft.setMinSpeed(MIN_SPEED)
        FrontRight.setMinSpeed(MIN_SPEED)
        RearLeft.setMinSpeed(MIN_SPEED)
        RearRight.setMinSpeed(MIN_SPEED)
        return this
    }

    fun setMaxSpeed(speed: Double): KTDriveMotorSystem {
        this.MAX_SPEED = speed
        FrontLeft.setMaxSpeed(MAX_SPEED)
        FrontRight.setMaxSpeed(MAX_SPEED)
        RearLeft.setMaxSpeed(MAX_SPEED)
        RearRight.setMaxSpeed(MAX_SPEED)
        return this
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
        setModel(MotorModel.NEVEREST40)
        WheelDiameter = 3.937
        setMinSpeed(MIN_SPEED)
    }

    protected fun resetEncoders(): KTDriveMotorSystem {
        FrontLeft.resetEncoder()
        FrontRight.resetEncoder()
        RearLeft.resetEncoder()
        RearRight.resetEncoder()
        return this
    }

    fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): KTDriveMotorSystem {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior)
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior)
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior)
        RearRight.setZeroPowerBehavior(zeroPowerBehavior)
        return this
    }

    protected fun setRunMode(runMode: DcMotor.RunMode): KTDriveMotorSystem {
        FrontLeft.setRunMode(runMode)
        FrontRight.setRunMode(runMode)
        RearLeft.setRunMode(runMode)
        RearRight.setRunMode(runMode)
        return this
    }

    fun setGearedType(gearedType: GearedType): KTDriveMotorSystem {
        this.gearedType = gearedType
        return this
    }

    protected fun setModel(model: MotorModel): KTDriveMotorSystem {
        FrontLeft.setModel(model)
        FrontRight.setModel(model)
        RearLeft.setModel(model)
        RearRight.setModel(model)
        this.model = model
        this.CPR = model.CPR
        return this
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

    protected fun setTargets(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setTarget(fl)
        FrontRight.setTarget(fr)
        RearLeft.setTarget(rl)
        RearRight.setTarget(rr)
    }

    protected fun inches_to_clicks(inches: Double): Double {
        val circumference = WheelDiameter * Math.PI
        return CPR / circumference * inches

    }

    fun stopMotors() {
        FrontLeft.stopMotor()
        FrontRight.stopMotor()
        RearLeft.stopMotor()
        RearRight.stopMotor()
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

    fun avgSpeed(): Double {
        return (Math.abs(FrontLeft.getPower()) + Math.abs(FrontRight.getPower()) + Math.abs(RearLeft.getPower()) + Math.abs(RearRight.getPower())) / 4
    }

    internal fun calculateAdjustedTarget(target: Double, direction: TurnDirection): Double {
        when(direction) {
            TurnDirection.LEFT -> return target - GYRO_LATENCY_OFFSET - GYRO_SLOW_MODE_OFFSET
            TurnDirection.RIGHT -> return target + GYRO_LATENCY_OFFSET + GYRO_SLOW_MODE_OFFSET
        }
    }

    internal fun calculateFinalTarget(target: Double, direction: TurnDirection): Double {
        when(direction) {
            TurnDirection.LEFT -> return target - GYRO_LATENCY_OFFSET
            TurnDirection.RIGHT -> return target + GYRO_LATENCY_OFFSET
        }
    }

    internal fun calculateProportion(current: Double, start: Double, distance: Double): Double {
        val remaining = current - start
        val proportion = (1 - (Math.abs((remaining) / distance))) * 0.75 + 0.25
        return proportion
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



}