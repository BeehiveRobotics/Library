package org.BeehiveRobotics.Library.Tests

import org.BeehiveRobotics.Library.Util.BROpMode
class ControllerTest: BROpMode() {
    override fun initialize() {
        setOpModeType(OpModeType.TeleOp)
    }
    override fun run() {
        addData("Left Stick X", controller1.leftStickX().toString())
        addData("Left Stick Y", controller1.leftStickY().toString())
        addData("Right Stick X", controller1.rightStickX().toString())
        addData("Right Stick Y", controller1.rightStickY().toString())
        addData("A", controller1.a().toString())
        addData("B", controller1.b().toString())
        addData("X", controller1.x().toString())
        addData("Y", controller1.y().toString())
        addData("Dpad up", controller1.dpad_up().toString())
        addData("Dpad down", controller1.dpad_down().toString())
        addData("Dpad left", controller1.dpad_left().toString())
        addData("Dpad right", controller1.dpad_right().toString())
        addData("Left Bumper", controller1.leftBumper().toString())
        addData("Right Bumper", controller1.rightBumper().toString())
        addData("Left Trigger", controller1.leftTrigger().toString())
        addData("Right Trigger", controller1.rightTrigger().toString())
        addData("A toggle", controller1.aToggle().toString())
        addData("B toggle", controller1.bToggle().toString())
        addData("X toggle", controller1.xToggle().toString())
        addData("Y toggle", controller1.yToggle().toString())
        addData("Dpad up toggle", controller1.dpad_upToggle().toString())
        addData("Dpad down toggle", controller1.dpad_downToggle().toString())
        addData("Dpad left toggle", controller1.dpad_leftToggle().toString())
        addData("Dpad right toggle", controller1.dpad_rightToggle().toString())
        addData("Left bumper toggle", controller1.leftBumperToggle().toString())
        addData("Right bumper toggle", controller1.rightBumperToggle().toString())
        updateTelemetry()
    }
    override fun end() {

    }
}