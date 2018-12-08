package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.util.ElapsedTime

class BRTelemetry(private val opMode: BROpMode) {
    fun addLine(line: String) {
        opMode.telemetry.addLine(line)
    }
    fun showLine(line: String) {
        addLine(line)
        update()
    }
    fun addData(title: String, value: Any) {
        opMode.telemetry.addData(title, value)
    }
    fun showData(title: String, value: Any) {
        addData(title, value)
        update()
    }
    fun update() = opMode.telemetry.update()
}