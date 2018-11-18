package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.util.ElapsedTime

class BRTelemetry(private val opMode: BROpMode): Runnable {
    var hasNew = false
    fun addLine(line: String, showImmediately: Boolean = false) {
        hasNew = true
        opMode.telemetry.addLine(line)
        if(showImmediately) update()
    }
    fun addData(title: String, value: Any, showImmediately: Boolean = false) {
        hasNew = true
        opMode.telemetry.addData(title, value)
        if(showImmediately) update()
    }
    fun update() {
        opMode.telemetry.update()
        hasNew = false
    }
    init {
        val thread = Thread(this)
        thread.start()
    }
    override fun run() {
        while(!opMode.opModeIsActive()) {}
        val time = ElapsedTime()
        var i = 0
        while(opMode.opModeIsActive()) {
            while(time.milliseconds() < i * 250) {}
            if(hasNew) update()
            else time.reset()
            i.inc()
            if(i == Integer.MAX_VALUE) i = 0
        }
    }
}