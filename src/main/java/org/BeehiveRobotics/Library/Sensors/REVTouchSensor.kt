package org.BeehiveRobotics.Library.Sensors

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.DigitalChannel
class REVTouchSensor(private val opMode: BROpMode, private val name: String) {
    private val sensor: DigitalChannel = opMode.hardwareMap.get(DigitalChannel::class.java, name)
    fun isPressed(): Boolean {
        return !sensor.state
    }
}