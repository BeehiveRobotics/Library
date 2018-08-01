package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.BeehiveRobotics.Library.Util.Controller

abstract class BROpMode(private val opModeType: OpModeType): LinearOpMode() {

    protected lateinit var controller1: Controller
    protected lateinit var controller2: Controller

    enum class OpModeType {
        Autonomous, TeleOp
    }

    abstract fun initialize()

    abstract fun run()

    fun showLine(line: String) {
        telemetry.addLine(line)
        telemetry.update()
    }
    
    fun addLine(line: String) = telemetry.addLine(line)

    fun showData(title: String, value: String) {
        telemetry.addData(title, value)
        telemetry.update()
    }

    fun addData(title: String, value: String) = telemetry.addData(title, value)

    fun updateTelemetry() = telemetry.update()

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        try {
            showLine("Not ready to start")
            controller1 = Controller(gamepad1)
            controller2 = Controller(gamepad2)
            initialize()
            showLine("Ready to Start")
            waitForStart()
            when (opModeType) {
                BROpMode.OpModeType.TeleOp -> {
                    while (opModeIsActive()) {
                        run()
                        controller1.update()
                        controller2.update()
                    }
                }
                BROpMode.OpModeType.Autonomous -> run()
            }
        } finally {}
    }
}
