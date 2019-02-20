package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.RobotSystem

class Motor(private val opMode: BROpMode, val name: String): RobotSystem(opMode), Runnable {
    val motor: DcMotor = opMode.hardwareMap.get(DcMotor::class.java, name)
    var RAMPING_COEFFICIENT = 0.8
    var MIN_SPEED = 0.2
        set(speed) {
            field = Math.abs(speed)
        }
    var MAX_SPEED = 1.0
        set(speed) {
            field = Math.abs(speed)
        }
    var MAX_NO_SLIP_SPEED = 1.0
        set(value) {
            field = Math.abs(value)
        }
    var target = 0.0
        set(target) {
            resetEncoder()
            this.current = currentPosition
            field = Math.abs(target)
        }
    var time = 0L
    private var current = 0.0
    var power
        set(value) {
            if(value==0.0 || !opMode.opModeIsActive()) {
                motor.power = 0.0
                return
            }
            when(rampingType) {
                RampingType.None -> {
                    this.current = currentPosition
                    motor.power = value
                    return
                }
                RampingType.ConstantJerk -> {
                    this.current = currentPosition
                    val k = 4.0 / target
                    val calcPower = k * current * (1 - (current/target)) * value + Double.MIN_VALUE
                    val expoPower = Range.clip(Math.pow(Math.abs(calcPower), RAMPING_COEFFICIENT), MIN_SPEED, MAX_SPEED)
                    if(value < 0) motor.power = -expoPower
                    if(value > 0) motor.power = expoPower
                    return
                }
                RampingType.Piecewise -> {
                    current = currentPosition
                    
                    if(target < RAMP_CLICKS_PROPORTION*CPR*2) { //no room to ramp
                        val calcSpeed = Math.min(Math.abs(value), MAX_NO_SLIP_SPEED)
                        motor.power = if(value>0) calcSpeed else -calcSpeed
                        return
                    }
                    if(current < RAMP_CLICKS_PROPORTION*CPR) { //Ramp up
                        val calcSpeed = Math.pow(current/CPR, 0.5)*(Math.abs(value)-MIN_SPEED)+MIN_SPEED
                        motor.power = if(value>0) calcSpeed else -calcSpeed
                        return
                    }
                    if(current > RAMP_CLICKS_PROPORTION*CPR && target - current > RAMP_CLICKS_PROPORTION*CPR) { //linear period
                        motor.power = value
                        return
                    }
                    if(target - current < RAMP_CLICKS_PROPORTION*CPR) { //ramp down
                        val calcSpeed = Math.pow((target-current)/CPR, 0.5)*(Math.abs(value)-0.325) + 0.325
                        motor.power = if(value>0) calcSpeed else -calcSpeed
                        return
                    }
                }
                RampingType.LinearDown -> {
                    current = currentPosition
                    val calcSpeed = (1 - current/target) * (Math.abs(value)-MIN_SPEED) + MIN_SPEED
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                    return
                }
                RampingType.LinearUp -> {
                    current = currentPosition
                    val calcSpeed = (current/target) * (Math.abs(value)-MIN_SPEED) + MIN_SPEED
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                    return
                }
            }
        }
        get() = motor.power
    var RAMP_CLICKS_PROPORTION = 0.5
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
    val RPM: Double
        get() = model.RPM
    var currentPosition = 0.0
        private set
        get() = Math.abs(this.motor.currentPosition.toDouble())
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        set(zeroPowerBehavior) {
            this.motor.zeroPowerBehavior = zeroPowerBehavior
            motor.power = 0.0
        }
        get() = this.motor.zeroPowerBehavior
    var direction: DcMotorSimple.Direction
        set(value) {
            motor.direction = value
        }
        get() = motor.direction
        
    enum class MotorModel(val CPR: Double, val RPM: Double) {
        NEVEREST20(537.6, 333.3), 
        NEVEREST40(1120.0, 160.0), 
        NEVEREST60(1680.0, 106.6)
    }

    enum class Tasks {
        RunToPosition, RunForTime, Stop
    }

    enum class RampingType {
        None, ConstantJerk, Piecewise, LinearDown, LinearUp
    }
    var rampingType = RampingType.None
    
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
                this.power = targetPower
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
            motor.power = targetPower
            while(runTime.milliseconds() < this.time) if(!opMode.opModeIsActive()) continue
            this.stopMotor()
        }
        isBusy = false
    }

    fun stopMotor() {
        this.motor.power = 0.0
    }

    fun isAtTarget(): Boolean = Math.abs(currentPosition) + 30 >= Math.abs(target)

    override fun toString(): String =
        "Target clicks: $target\n" + 
        "Target power: $targetPower\n" +  
        "Current power: ${motor.power}\n"
        
    override fun run() {
        isBusy = true
        when(this.task) {
            Tasks.RunToPosition -> {
                while(!this.isAtTarget()) {
                    if(!opMode.opModeIsActive()) return                    
                    this.power = this.targetPower
                }
                this.stopMotor()
                task = Tasks.Stop
                isBusy = false
                Thread.currentThread().interrupt()
            }
            Tasks.RunForTime -> {
                val runTime = ElapsedTime()
                runTime.reset()
                this.motor.power = targetPower
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