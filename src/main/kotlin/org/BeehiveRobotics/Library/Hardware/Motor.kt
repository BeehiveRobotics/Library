package org.BeehiveRobotics.Library.Hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import com.qualcomm.robotcore.util.ElapsedTime
import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Systems.SubSystem
import org.BeehiveRobotics.Library.Systems.HardwareDevice

class Motor(name: String, private val opMode: BROpMode, var model: MotorModel = MotorModel.NEVEREST20, var rampingType: RampingType = RampingType.None): HardwareDevice(name, opMode), Runnable {
    val motor = opMode.hardwareMap.get(DcMotor::class.java, name)
    enum class MotorModel(val CPR: Double, val RPM: Double) {
        NEVEREST20(537.6, 333.3), 
        NEVEREST40(1120.0, 160.0), 
        NEVEREST60(1680.0, 106.6)
    }
    enum class Tasks {
        RunToPosition, MoveClicks, RunForTime, Stop, None
    }
    var currentTask = Tasks.None
    enum class RampingType {
        SCurve, Piecewise, LinearDown, LinearUp, ExpoDown, ExpoUp, None
    }
    enum class ZeroPowerBehavior {
        BRAKE, COAST
    }
    var zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        set(value) {
            when(value) {
                ZeroPowerBehavior.BRAKE -> motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
                ZeroPowerBehavior.COAST -> motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            }
            field = value
        }
    enum class Direction {
        NORMAL, REVERSE
    }
    var direction = Direction.NORMAL
        set(value) {
            when(value) {
                Direction.NORMAL -> motor.direction = DcMotorSimple.Direction.FORWARD
                Direction.REVERSE -> motor.direction = DcMotorSimple.Direction.REVERSE
            }
            field = value
        }
    var minSpeed = 0.0
    var maxSpeed = 0.0
    var currentPosition = motor.currentPosition
        get() = motor.currentPosition
    var target = 0.0
    var rampClicksProportion = 2.0
    var rampExpo = 0.5
    var power
        set(value) {
            if(value==0.0 || opMode.opModeIsActive()) {
                    motor.power = 0.0
                    return
                }
            val current = currentPosition
            when(rampingType) {
                RampingType.SCurve -> {
                    val k = 4.0 / target
                    val calcPower = k * current * (1 - (current/target)) * value + Double.MIN_VALUE
                    val expoPower = Range.clip(Math.pow(Math.abs(calcPower), rampExpo), minSpeed, maxSpeed)
                    motor.power = if(value>0) expoPower else -expoPower
                }
                RampingType.Piecewise -> {
                    val rampingClicks = rampClicksProportion*model.CPR
                    if(target < rampingClicks*2) {
                        motor.power = value
                        return
                    }
                    if(current < rampingClicks) {
                        val calcSpeed = Math.pow(current / rampingClicks, rampExpo) * (Math.abs(value) - minSpeed) + minSpeed
                        motor.power = if(value>0) calcSpeed else -calcSpeed
                        return
                    }
                    if(current > rampingClicks && target - current>rampingClicks) {
                        motor.power = value
                        return
                    }
                    if(target - current  < rampingClicks) {
                        val calcSpeed = Math.pow((target-current)/rampingClicks, rampExpo) * (Math.abs(value) - minSpeed) + minSpeed
                        motor.power = if(value>0) calcSpeed else -calcSpeed
                        return
                    }
                }
                RampingType.LinearDown -> {
                    val calcSpeed = (1 - current / target) * (Math.abs(value)-minSpeed) + minSpeed
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                }
                RampingType.LinearUp -> {
                    val calcSpeed = (current / target) * (Math.abs(value)-minSpeed) + minSpeed
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                }
                RampingType.ExpoDown -> {
                    val calcSpeed = Math.pow((target-current)/target, rampExpo) * (Math.abs(value) - minSpeed) + minSpeed
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                }
                RampingType.ExpoUp -> {
                    val calcSpeed = Math.pow(current/target, rampExpo) * (Math.abs(value) - minSpeed) + minSpeed
                    motor.power = if(value>0) calcSpeed else -calcSpeed
                }
                RampingType.None -> {
                    motor.power = value
                }
            }
            return 
        }
        get() = motor.power

    var atTarget = true
        get() = if(power>0) power>=target else if (power<0) power<=target else true
    
    
    fun resetEncoder() {
        val initialBehavior = motor.mode
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun toString(): String {
        return "" + 
            "Power: $power\n" + 
            "Target: $target"
    }
    override fun run() {

    }
}