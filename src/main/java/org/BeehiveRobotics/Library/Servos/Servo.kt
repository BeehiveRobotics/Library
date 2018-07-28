package org.BeehiveRobotics.Library.Servos

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.BeehiveRobotics.Library.Systems.RobotSystem

class Servo(private val opMode: BROpMode, val name: String): RobotSystem(opMode), Runnable {

    private val servo: Servo = opMode.hardwareMap.get(Servo::class.java, name)
    private var servoPosition: Double = 0.0
    private var servoSpeed: Double = 1.0
    private var rangeOfMotion: Double = 180.0
    private var thread: Thread = Thread(this)

    enum class ServoModel(val RPM: Double) {
        REV(60.0) // TODO: TEST THIS NUMBER. THIS IS JUST A FILLER FOR NOW
    }

    init {
        thread.start()
    }

    fun setDirection(direction: com.qualcomm.robotcore.hardware.Servo.Direction): org.BeehiveRobotics.Library.Servos.Servo {
        servo.setDirection(direction)
        return this
    }

    fun setSpeed(speed: Double): org.BeehiveRobotics.Library.Servos.Servo {
        this.servoSpeed = Range.clip(Math.abs(speed), 0.0, 1.0)
        return this
    }

    fun setRange(degrees: Double): org.BeehiveRobotics.Library.Servos.Servo {
        this.rangeOfMotion = degrees
        return this
    }
    
    fun setPosition(position: Double) {
        this.servoPosition = position
    }
    
    fun getPosition(): Double {
        return servo.position
    }
    
    override fun run() {
        var previousTarget = this.servoPosition
        while(opMode.opModeIsActive()) {
            if(previousTarget != this.servoPosition) {
                val distance = getPosition() - this.servoPosition

                previousTarget = this.servoPosition
            }

        }
    }
}
//TODO: Finish this with speed control and more Kotlinization, kinda like ../Motors/Motor.kt has