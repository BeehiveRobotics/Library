package org.BeehiveRobotics.Library.Sensors

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.ColorSensor

class REVColorSensor(private val opMode: BROpMode, val name: String) {
    private val cs = opMode.hardwareMap.get(ColorSensor::class.java, name)
    var red = 0
        private set
        get() = cs.red()
    var green = 0
        private set
        get() = cs.green()
    var blue = 0
        private set
        get() = cs.blue()
    
    fun enableLED(value: Boolean = true) = cs.enableLed(value)
}