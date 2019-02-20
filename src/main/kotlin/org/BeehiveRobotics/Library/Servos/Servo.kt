package org.BeehiveRobotics.Library.Servos

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.BeehiveRobotics.Library.Systems.RobotSystem
import com.qualcomm.robotcore.util.ElapsedTime

class Servo(private val opMode: BROpMode, val name: String, var targetPosition: Double = 0.0): RobotSystem(opMode), Runnable {
    enum class ServoModel(val RPM: Double) {
        REV(20.0) // TODO: TEST THIS NUMBER. THIS IS JUST A FILLER FOR NOW
    }
    private val servo: Servo = opMode.hardwareMap.get(Servo::class.java, name)
    var model: ServoModel = ServoModel.REV
    var MIN_POSITION = 0.0
    var MAX_POSITION = 1.0
    var position: Double
        set(value) {
            servo.position = Range.clip(value, MIN_POSITION, MAX_POSITION)
        }
        get() = servo.position
    var speed: Double = 1.0
        set(value) {
            Range.clip(Math.abs(value), 0.0, 1.0)
        }
    var degrees: Double = 180.0 // the degrees it can move (not from 0.0 to 1.0, from whatever limits the user sets)
    private val thread: Thread = Thread(this)
    var direction // as in reverse or forward
        set(value) {
            servo.direction = value
        }
        get() = servo.direction

    init {
        thread.start() // it can just constantly run a thread, why not
    }
    override fun run() {
        val time = ElapsedTime()
        var index = 1
        val updateSpeed = 0.01 //this many seconds, it updates
        val increment = 1000 * updateSpeed //how many milliseconds each update takes
        while(opMode.opModeIsActive()) {
            val RPS = model.RPM/60 //rotations per second
            val DPS = RPS*360 //degrees per second
            val DPI = DPS/increment //degreees per increment
            val PPI = DPI/degrees //proportion per increment
            val CPPI = PPI*speed //calculated ppi, taking into account speed
            position = if(targetPosition<position) position - CPPI else if(targetPosition>position) position + CPPI else position
            sleep(((index * increment) - time.milliseconds()).toLong())
            index++
            if(position == targetPosition) index = 1
        }
    } 
}
