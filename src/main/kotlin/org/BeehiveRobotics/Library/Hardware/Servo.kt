package org.BeehiveRobotics.Library.Servos

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.Servo as QServo
import com.qualcomm.robotcore.util.Range
import org.BeehiveRobotics.Library.Systems.SubSystem
import com.qualcomm.robotcore.util.ElapsedTime

class Servo(val name: String, opMode: BROpMode, var defaultPosition: Double = 0.0): SubSystem(opMode) {
    private val servo: QServo = opMode.hardwareMap.get(QServo::class.java, name)
    var position: Double = defaultPosition
        set(value) {
            servo.position = value
            field = value
        }
    enum class Direction {
        NORMAL, REVERSE
    }
    var direction = Direction.NORMAL
        set(value) {
            when(value) {
                Direction.NORMAL -> servo.direction = QServo.Direction.FORWARD
                Direction.REVERSE -> servo.direction = QServo.Direction.REVERSE
            }
            field = value
        }
}
