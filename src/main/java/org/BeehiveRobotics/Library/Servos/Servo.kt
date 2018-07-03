package org.BeehiveRobotics.Library.Servos

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.Servo

class Servo(private val opMode: BROpMode, val name: String): Runnable {
    private val servo: Servo = opMode.hardwareMap.get(Servo::class.java, name)
    fun setPosition(position: Double) {
        servo.setPosition(position)
    }
    fun setDirection(direction: Servo.Direction): org.BeehiveRobotics.Library.Servos.Servo {
        servo.setDirection(direction)
        return this
    }
    override fun run() {

    }
}