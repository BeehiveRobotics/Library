package org.BeehiveRobotics.Library.Robots

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime

abstract class Robot(private val opMode: BROpMode) {
    abstract fun waitUntilNotBusy()
    fun sleep(milliseconds: Long) {
        val time = ElapsedTime()
        time.reset()
        while(time.milliseconds() < milliseconds) {
            if(!opMode.opModeIsActive()) {
                return
            }
        }
    }
}
