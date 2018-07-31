package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.RobotSystem

class Motor(private val opMode: BROpMode, val name: String): RobotSystem(opMode), Runnable {
    private val motor: DcMotor = opMode.hardwareMap.get(DcMotor::class.java, name)
    private val RAMP_LOG_EXPO = 0.8
    var MIN_SPEED = 0.2
        set(speed) {
            this.MIN_SPEED = Math.abs(speed)
        }
    var MAX_SPEED = 1.0
        set(speed) {
            this.MAX_SPEED = Math.abs(speed)
        }
    var target = 0.0
        set(target) {
            resetEncoder()
            this.current = currentPosition
            this.target = Math.abs(target)
        }
    private var current = 0.0
    var power = 0.0
        set(value) {
            if (!this.opMode.opModeIsActive() || value == 0.0 || isAtTarget()) stopMotor()
            this.current = Math.abs(currentPosition)
            val k = 4.0 / target
            val calculated_power = k * this.current * (1 - (this.current / this.target)) * value + java.lang.Double.MIN_VALUE
            val expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO)
            if (power < 0) this.rawPower = -expo_speed
            this.rawPower = expo_speed

        }
    private var task = Tasks.Stop
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
    var rawPower: Double 
        set(value) {
            if (opMode.opModeIsActive()) {
                if (value > 0) this.motor.power = Range.clip(value, MIN_SPEED, MAX_SPEED)
                else if (value < 0) this.motor.power = Range.clip(value, -MAX_SPEED, -MIN_SPEED)
                else stopMotor()
            } 
            else stopMotor()
        }
        get() = motor.power
    var direction: DcMotorSimple.Direction
        set(value) {
            motor.direction = value
        }
        get() = motor.direction
        
    enum class MotorModel(val CPR: Double) {
        NEVEREST20(537.6), 
        NEVEREST40(1120.0), 
        NEVEREST60(1680.0)
    }

    enum class Tasks {
        RunToPosition, Stop
    }
    
    init {
        this.model = MotorModel.NEVEREST40
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        resetEncoder()
    }

    internal fun resetEncoder(): Motor {
        val initialBehavior = this.runMode
        this.runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.runMode = initialBehavior
        this.current = 0.0
        return this
    }

    internal fun runToTarget(target: Double, power: Double, waitForCompletion: Boolean = true) {
        isBusy = true
        this.target = Math.abs(target)
        this.power = power
        if (!waitForCompletion) {
            val thread = Thread(this)
            thread.start()
        } else {
            while (!this.isAtTarget()) {
                if(!opMode.opModeIsActive()) {
                    return
                }
                this.power = power
            }
        }
        isBusy = false
    }

    fun stopMotor() {
        this.motor.power = 0.0
    }

    fun isAtTarget(): Boolean = Math.abs(current) >= Math.abs(target)

    override fun run() {
        isBusy = true
        when(this.task) {
            Tasks.RunToPosition -> {
                while(!this.isAtTarget()) {
                    if(!opMode.opModeIsActive()) return                    
                    this.power = this.power
                }
                this.stopMotor()
                Thread.currentThread().interrupt()
            }
            Tasks.Stop -> this.stopMotor()            
        }
        isBusy = false
    }
}