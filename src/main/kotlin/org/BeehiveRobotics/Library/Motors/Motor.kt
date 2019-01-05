package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.RobotSystem

class Motor(private val opMode: BROpMode, val name: String): RobotSystem(opMode), Runnable {
    private val motor: DcMotor = opMode.hardwareMap.get(DcMotor::class.java, name)
    var RAMPING_COEFFICIENT = 0.8
    var MIN_SPEED = 0.2
        set(speed) {
            field = Math.abs(speed)
        }
    var MAX_SPEED = 1.0
        set(speed) {
            field = Math.abs(speed)
        }
    var target = 0.0
        set(target) {
            resetEncoder()
            this.current = currentPosition
            field = Math.abs(target)
        }
    var time = 0L
    private var current = 0.0
    var constantRampPower = 0.0
        set(value) {
            if (!this.opMode.opModeIsActive() || value == 0.0 || isAtTarget()) stopMotor()
            this.current = currentPosition
            val k = 4.0 / target
            val calculated_power = k * this.current * (1 - (this.current / this.target)) * value + java.lang.Double.MIN_VALUE
            val expo_speed = Range.clip(Math.pow(Math.abs(calculated_power), RAMPING_COEFFICIENT), MIN_SPEED, MAX_SPEED)
            if (value < 0) this.rawPower = -expo_speed
            else if (value > 0) this.rawPower = expo_speed
            else stopMotor()
            field = value
        }
    var piecewiseRampPower = 0.0
        set(value) {
            this.current = currentPosition
            if(target < RAMP_CLICKS_PROPORTION*CPR) {
                rawPower = value
                return
            }
            if(current < RAMP_CLICKS_PROPORTION*CPR) {
                val calcSpeed = Math.pow(current/CPR, 0.5)*(Math.abs(value)-MIN_SPEED)+MIN_SPEED
                rawPower = if(value>0) calcSpeed else -calcSpeed
                return
            }
            if(target - current < RAMP_CLICKS_PROPORTION*CPR) {
                val calcSpeed = Math.pow((target-current)/CPR, 0.5)*(Math.abs(value)-MIN_SPEED)+MIN_SPEED
                rawPower = if(value>0) calcSpeed else -calcSpeed
                return
            }
            rawPower = value
        }
    val RAMP_CLICKS_PROPORTION = 0.5
    var targetPower = 0.0
    private var task = Tasks.Stop
    var runMode: DcMotor.RunMode
        set(runMode) {
            this.motor.mode = runMode                                                 
        }
        get() = this.motor.mode
    var model = MotorModel.NEVEREST40
    val CPR: Double
        get() = model.CPR
    var currentPosition = 0.0
        private set
        get() = Math.abs(this.motor.currentPosition.toDouble())
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        set(zeroPowerBehavior) {
            this.motor.zeroPowerBehavior = zeroPowerBehavior
            this.rawPower = 0.0
        }
        get() = this.motor.zeroPowerBehavior
    var rawPower: Double 
        set(value) {
            if(opMode.opModeIsActive()) this.motor.power = value
            else stopMotor()
        }
        get() = this.motor.power
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
        RunToPosition, RunForTime, Stop
    }
    
    init {
        this.model = MotorModel.NEVEREST40
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        resetEncoder()
    }

    fun resetEncoder(): Motor {
        val initialBehavior = this.runMode
        this.runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.runMode = initialBehavior
        this.current = 0.0
        return this
    }

    fun runToPosition(targetPower: Double, targetClicks: Double, waitForCompletion: Boolean = true) {
        resetEncoder()
        isBusy = true
        this.target = Math.abs(targetClicks)
        this.targetPower = targetPower
        if (!waitForCompletion) {
            task = Tasks.RunToPosition
            val thread = Thread(this)
            thread.start()
        } else {
            while (!this.isAtTarget()) {
                if(!opMode.opModeIsActive()) {
                    return
                }
                this.constantRampPower = targetPower
            }
            stopMotor()
        }
        isBusy = false
    }

    fun runForTime(targetPower: Double, time: Long, waitForCompletion: Boolean = true) {
        isBusy = true
        this.time = time
        this.targetPower = targetPower
        if(!waitForCompletion) {
            task = Tasks.RunForTime
            val thread = Thread(this)
            thread.start()
        } else {
            val runTime = ElapsedTime()
            this.rawPower = targetPower
            while(runTime.milliseconds()<this.time) if(!opMode.opModeIsActive()) continue
            this.stopMotor()
        }
        isBusy = false
    }

    fun stopMotor() {
        this.motor.power = 0.0
    }

    fun isAtTarget(): Boolean = Math.abs(currentPosition) >= Math.abs(target)

    override fun toString(): String =
        "Target clicks: $target\n" + 
        "Target power: $targetPower\n" +  
        "Current power: $rawPower\n"
        
    override fun run() {
        isBusy = true
        when(this.task) {
            Tasks.RunToPosition -> {
                while(!this.isAtTarget()) {
                    if(!opMode.opModeIsActive()) return                    
                    this.constantRampPower = this.targetPower
                }
                this.stopMotor()
                task = Tasks.Stop
                isBusy = false
                Thread.currentThread().interrupt()
            }
            Tasks.RunForTime -> {
                val runTime = ElapsedTime()
                this.rawPower = targetPower
                while(runTime.milliseconds()<this.time) if(!opMode.opModeIsActive()) continue
                this.stopMotor()
                task = Tasks.Stop
                isBusy = false
                Thread.currentThread().interrupt()
            }
            Tasks.Stop -> this.stopMotor()            
        }
    }
}