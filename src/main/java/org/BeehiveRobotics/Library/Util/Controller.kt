package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.hardware.Gamepad

class Controller(val gamepad: Gamepad) {
    private var aPrev = false
    private var bPrev = false
    private var xPrev = false
    private var yPrev = false
    private var leftBumperPrev = false
    private var rightBumperPrev = false
    private var dpad_upPrev = false
    private var dpad_downPrev = false
    private var dpad_leftPrev = false
    private var dpad_rightPrev = false

    fun leftStickX(): Double = gamepad.left_stick_x.toDouble()
    fun leftStickY(): Double = (-gamepad.left_stick_y).toDouble()
    fun rightStickX(): Double = gamepad.right_stick_x.toDouble()
    fun rightStickY(): Double = (-gamepad.right_stick_y).toDouble()

    fun leftTrigger(): Double = gamepad.left_trigger.toDouble()
    fun rightTrigger(): Double = gamepad.right_trigger.toDouble()

    fun leftBumper(): Boolean = gamepad.left_bumper
    fun rightBumper(): Boolean = gamepad.right_bumper

    fun dpad_up(): Boolean = gamepad.dpad_up
    fun dpad_down(): Boolean = gamepad.dpad_down
    fun dpad_left(): Boolean = gamepad.dpad_left
    fun dpad_right(): Boolean = gamepad.dpad_right

    fun a(): Boolean = gamepad.a
    fun b(): Boolean = gamepad.b
    fun x(): Boolean = gamepad.x
    fun y(): Boolean = gamepad.y

    fun aToggle(): Boolean = a() && !aPrev
    fun bToggle(): Boolean = b() && !bPrev
    fun xToggle(): Boolean = x() && !xPrev
    fun yToggle(): Boolean = y() && !yPrev

    fun leftBumperToggle(): Boolean = leftBumper() && !leftBumperPrev
    fun rightBumperToggle(): Boolean = rightBumper() && !rightBumperPrev

    fun dpad_upToggle(): Boolean = dpad_up() && !dpad_upPrev
    fun dpad_downToggle(): Boolean = dpad_down() && !dpad_downPrev
    fun dpad_leftToggle(): Boolean = dpad_left() && !dpad_leftPrev
    fun dpad_rightToggle(): Boolean = dpad_right() && !dpad_rightPrev

    fun update() {
        aPrev = a()
        bPrev = b()
        xPrev = x()
        yPrev = y()

        leftBumperPrev = leftBumper()
        rightBumperPrev = rightBumper()

        dpad_upPrev = dpad_up()
        dpad_downPrev = dpad_down()
        dpad_leftPrev = dpad_left()
        dpad_rightPrev = dpad_right()
    }
}
