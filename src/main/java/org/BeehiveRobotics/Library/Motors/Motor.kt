package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode

class Motor(val opMode: BROpMode, val name: String): Runnable {
    private val RAMP_LOG_EXPO = 0.8
    var MIN_SPEED = 0.2
        set(speed) {
            this.MIN_SPEED = Math.abs(speed)
        }
    var MAX_SPEED = 1.0
        set(speed) {
            this.MAX_SPEED = Math.abs(speed)
        }
    var target: Double = 0.0
        set(target) {
            resetEncoder()
            this.current = currentPosition
            this.target = Math.abs(target)
        }
    private var current: Double = 0.0
    var power: Double = 0.0
        private set 
    private val motor: DcMotor = opMode.hardwareMap.get(DcMotor::class.java, name)
    private var task: Tasks = Tasks.Stop
    var runMode: DcMotor.RunMode
        set(runMode) {
            this.motor.mode = runMode
        }
        get() = this.motor.mode
    var model = MotorModel.NEVEREST40
    var currentPosition = 0.0
        private set
        get() = this.motor.currentPosition.toDouble()
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        set(zeroPowerBehavior) {
            this.motor.zeroPowerBehavior = zeroPowerBehavior
        }
        get() = this.motor.zeroPowerBehavior

    enum class MotorModel(val CPR: Double) {
        NEVEREST20(537.6), 
        NEVEREST40(1120.0), 
        NEVEREST60(1680.0)
    }

    enum class Tasks{
        RunToPosition, Stop
    }
    
    init {
        this.model = (MotorModel.NEVEREST40)
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        resetEncoder()
    }

    internal fun resetEncoder(): Motor {
        val initialBehavior = this.runMode
        this.runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.runMode = initialBehavior
        this.current = 0.0
        return this
    }

    internal fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): Motor {
        this.motor.zeroPowerBehavior = zeroPowerBehavior
        return this
    }

    internal fun runToTarget(target: Double, power: Double, waitForStop: Boolean) {
        this.target = Math.abs(target)
        this.power = power
        if (!waitForStop) {
            val thread = Thread(this)
            thread.start()
        } else {
            while (!this.isAtTarget()) {
                if(!opMode.opModeIsActive()) {
                    return
                }
                setPower(power)
            }
        }
    }

    internal fun runToTarget(target: Double, power: Double) {
        this.runToTarget(target, power, true)
    }

    fun setRawPower(power: Double) {
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
    fun getRawPower(): Double {
        return motor.power
    }

    fun setPower(power: Double): Boolean {
        this.power = power
        if (!this.opMode.opModeIsActive() || power == 0.0 || isAtTarget()) {
            stopMotor()
            return false
        }
        this.current = Math.abs(currentPosition)
        val k: Double = 4.0 / target
        val calculated_power: Double = k * this.current * (1 - (this.current / this.target)) * power + java.lang.Double.MIN_VALUE
        val expo_speed: Double = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO)
        if (power < 0) {
            setRawPower(-expo_speed)
            return true
        }
        setRawPower(expo_speed)
        return true
    }

    fun stopMotor() {
        this.motor.power = 0.0
    }

    internal fun getRunMode(): DcMotor.RunMode {
        return this.motor.mode
    }

    internal fun getName(): String {
        return this.name
    }

    fun isAtTarget(): Boolean {
        return Math.abs(current) >= Math.abs(target)
    }

    fun setDirection(direction: DcMotorSimple.Direction): Motor {
        this.motor.direction = direction
        return this

    }

    override fun run() {
        when(this.task) {
            Tasks.RunToPosition -> {
                while(!this.isAtTarget()) {
                    if(!opMode.opModeIsActive()) {
                        return
                    }
                    this.setPower(this.power)
                }
                this.stopMotor()
                Thread.currentThread().interrupt()
            }
            Tasks.Stop -> {
                this.stopMotor()
            }
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


}