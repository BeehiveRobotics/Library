package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Robots.Robot
import com.qualcomm.robotcore.util.ElapsedTime

abstract class RobotSystem(private val opMode: BROpMode) {
    var isBusy: Boolean = false
        protected set
    fun waitUntilNotBusy() {
        while(opMode.opModeIsActive() && isBusy) {}
    }
    fun sleep(milliseconds: Long) {
        val time = ElapsedTime()
        time.reset()
        while(time.milliseconds() < milliseconds) if(!opMode.opModeIsActive()) return
    }
}
