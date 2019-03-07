package org.BeehiveRobotics.Library.Sensors

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.hardware.ColorSensor
import android.graphics.Color

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

    fun RGB(): List<Int> {
        return listOf(red, green, blue)
    }
    fun HSV(): List<Float> {
        val rgb = RGB()
        var hsv = FloatArray(3)
        Color.RGBToHSV(rgb[0]/255, rgb[1]/255, rgb[2]/255, hsv)
        return listOf(hsv[0], hsv[1]*255, hsv[2]*255)
    }

    fun enableLED(value: Boolean = true) = cs.enableLed(value)

    override fun toString(): String {
        return "" + 
            "RGB: ($red, $green, $blue) \n" + 
            "HSV: (${HSV()[0]}, ${HSV()[1]}, ${HSV()[2]})"
    }
}