package org.BeehiveRobotics.Library.Servos

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range

class Servo(private val opMode: BROpMode, val name: String): Runnable {

    private val servo: Servo = opMode.hardwareMap.get(Servo::class.java, name)
    private var servoPosition: Double = 0.0
    private var servoSpeed: Double = 1.0
    private var rangeOfMotion: Double = 180.0
    private var thread: Thread = Thread(this)

    init {
        thread.start()
    }

    fun setRange(degrees: Double) {
        this.rangeOfMotion = degrees
    }
    
    fun setPosition(position: Double) {
        this.servoPosition = position
        servo.setPosition(position)
    }
    
    fun setSpeed(speed: Double) {
        this.servoSpeed = Math.abs(Range.clip(Math.abs(speed), 0.0, 1.0))
    }

    fun setDirection(direction: Servo.Direction): org.BeehiveRobotics.Library.Servos.Servo {
        servo.setDirection(direction)
        return this
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