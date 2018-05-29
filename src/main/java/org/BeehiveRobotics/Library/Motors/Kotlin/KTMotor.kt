package org.BeehiveRobotics.Library.Motors.Kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import org.BeehiveRobotics.Library.Util.BROpMode

class KTMotor constructor(opMode: BROpMode, name: String) : Runnable {
    private val RAMP_LOG_EXPO = 0.8
    private var MIN_SPEED = 0.2
    private var MAX_SPEED = 1.0
    private var model: MotorModel = MotorModel.NEVEREST40
    private var target: Double = 0.toDouble()
    private var current: Double = 0.toDouble()
    private var power: Double = 0.toDouble()
    private val name: String = name
    private val opMode : BROpMode = opMode
    private val motor : DcMotor = opMode.hardwareMap.get(DcMotor::class.java, name)

    internal fun setRunMode(runMode: DcMotor.RunMode): KTMotor {
        this.motor.mode = runMode
        return this
    }

    internal fun setModel(motorModel: MotorModel): KTMotor {
        this.model = motorModel
        return this
    }

    internal fun setMinSpeed(speed: Double): KTMotor {
        this.MIN_SPEED = Math.abs(speed)
        return this
    }

    fun setMaxSpeed(speed: Double): KTMotor {
        this.MAX_SPEED = Math.abs(speed)
        return this
    }

    internal fun resetEncoder(): KTMotor {
        val initialBehavior = this.getRunMode()
        this.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        this.setRunMode(initialBehavior)
        return this
    }

    internal fun getCurrentPosition(): Double {
        return this.motor.currentPosition.toDouble()
    }

    internal fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): KTMotor {
        this.motor.zeroPowerBehavior = zeroPowerBehavior
        return this
    }

    internal fun setTarget(target: Double) {
        this.target = Math.abs(target)
    }

    internal fun runToTarget(target: Double, power: Double, waitForStop: Boolean) {
        this.target = Math.abs(target)
        this.power = power
        if (!waitForStop) {
            val thread = Thread(this)
            thread.start()
        } else {
            while (!this.isAtTarget()) {
                setPower(power)
            }
        }
    }

    internal fun runToTarget(target: Double, power: Double) {
        this.runToTarget(target, power, true)
    }

    internal fun setRawPower(power: Double) {
        if (opMode.opModeIsActive()) {
            if (power > 0) {
                this.motor.power = Range.clip(power, MIN_SPEED, MAX_SPEED)
            } else if (power < 0) {
                this.motor.power = Range.clip(power, -MAX_SPEED, -MIN_SPEED)
            } else {
                stopMotor()
            }
        } else {
            stopMotor()
        }
    }

    internal fun setPower(power: Double) {
        this.power = power
        if (!this.opMode.opModeIsActive() || power == 0.0) {
            stopMotor()
            return
        }
        this.current = getCurrentPosition()
        val k = 4 / target
        val calculated_power = k * this.current * (1 - current / target) * power + java.lang.Double.MIN_VALUE
        val expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO)
        if (power < 0) {
            setRawPower(-expo_speed)
            return
        }
        setRawPower(expo_speed)
    }

    internal fun setPower(power: Double, current: Double, target: Double) {
        this.power = power
        if (!this.opMode.opModeIsActive() || power == 0.0) {
            stopMotor()
            return
        }
        this.current = current
        this.target = target
        val k: Double = 4 / target
        val calculated_power = k * this.current * (1 - current / target) * power + java.lang.Double.MIN_VALUE
        val expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO)
        if (power < 0) {
            setRawPower(-expo_speed)
            return
        }
        setRawPower(expo_speed)
    }

    internal fun stopMotor() {
        this.motor.power = 0.0
    }

    internal fun getRunMode(): DcMotor.RunMode {
        return this.motor.mode
    }

    internal fun getName(): String {
        return this.name
    }

    internal fun isAtTarget(): Boolean {
        return Math.abs(current) >= Math.abs(target)
    }

    fun setDirection(direction: DcMotorSimple.Direction): KTMotor {
        this.motor.direction = direction
        return this

    }

    override fun run() {
        while (!this.isAtTarget() && this.opMode.opModeIsActive()) {
            this.setPower(this.power)
        }
        this.stopMotor()
        Thread.currentThread().interrupt()
    }

}