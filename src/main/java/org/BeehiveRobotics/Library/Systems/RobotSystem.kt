package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.util.ElapsedTime

abstract class RobotSystem(private val opMode: BROpMode): Runnable {
    var isBusy: Boolean = false
    fun waitUntilNotBusy() {
        while(opMode.opModeIsActive()) {}
    }
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