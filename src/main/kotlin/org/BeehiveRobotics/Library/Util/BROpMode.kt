package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.BeehiveRobotics.Library.Util.Controller

abstract class BROpMode(private val opModeType: OpModeType): LinearOpMode() {

    protected lateinit var controller1: Controller
    protected lateinit var controller2: Controller

    val dashboard = BRTelemetry(this)

    enum class OpModeType {
        Autonomous, TeleOp
    }

    abstract fun initialize()

    abstract fun run()

    open fun end() {}

    open fun onStartPressed() {}

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        try {
            dashboard.addLine("Not ready to start")
            dashboard.update()
            controller1 = Controller(gamepad1)
            controller2 = Controller(gamepad2)
            initialize()
            dashboard.addLine("Ready to Start")
            dashboard.update()
            waitForStart()
            onStartPressed()
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
            end()
        } finally {}
    }
}