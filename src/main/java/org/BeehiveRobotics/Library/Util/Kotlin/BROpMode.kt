package org.BeehiveRobotics.Library.Util.Kotlin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.BeehiveRobotics.Library.Util.Kotlin.Controller

abstract class BROpMode: LinearOpMode() {

    private var opModeType = OpModeType.Autonomous

    protected lateinit var controller1: Controller
    protected lateinit var controller2: Controller

    enum class OpModeType {
        Autonomous, TeleOp
    }

    abstract fun initialize()

    abstract fun run()

    abstract fun end()

    protected fun setOpModeType(opModeType: OpModeType) {
        this.opModeType = opModeType
    }

    fun addLine(line: String) {
        telemetry.addLine(line)
        telemetry.update()
    }

    fun addData(title: String, data: String) {
        telemetry.addData(title, data)
        telemetry.update()
    }

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        try {
            controller1 = Controller(gamepad1)
            controller2 = Controller(gamepad2)
            initialize()
            waitForStart()
            when (opModeType) {
                BROpMode.OpModeType.TeleOp -> {
                    while (opModeIsActive()) {
                        run()
                        controller1.update()
                        controller2.update()
                    }
                    run()
                }
                BROpMode.OpModeType.Autonomous -> run()
            }
        } finally {
            end()
        }
    }
}
