package org.BeehiveRobotics.Library.Util.Kotlin

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

    fun leftStickX(): Double {
        return gamepad.left_stick_x.toDouble()
    }

    fun leftStickY(): Double {
        return (-gamepad.left_stick_y).toDouble()
    }

    fun rightStickX(): Double {
        return gamepad.right_stick_x.toDouble()
    }

    fun rightStickY(): Double {
        return (-gamepad.right_stick_y).toDouble()
    }

    fun leftTrigger(): Double {
        return gamepad.left_trigger.toDouble()
    }

    fun rightTrigger(): Double {
        return gamepad.right_trigger.toDouble()
    }

    fun leftBumper(): Boolean {
        return gamepad.left_bumper
    }

    fun rightBumper(): Boolean {
        return gamepad.right_bumper
    }

    fun dpad_up(): Boolean {
        return gamepad.dpad_up
    }

    fun dpad_down(): Boolean {
        return gamepad.dpad_down
    }

    fun dpad_left(): Boolean {
        return gamepad.dpad_left
    }

    fun dpad_right(): Boolean {
        return gamepad.dpad_right
    }

    fun a(): Boolean {
        return gamepad.a
    }

    fun b(): Boolean {
        return gamepad.b
    }

    fun x(): Boolean {
        return gamepad.x
    }

    fun y(): Boolean {
        return gamepad.y
    }

    fun aToggle(): Boolean {
        return a() && !aPrev
    }

    fun bToggle(): Boolean {
        return b() && !bPrev
    }

    fun xToggle(): Boolean {
        return x() && !xPrev
    }

    fun yToggle(): Boolean {
        return y() && !yPrev
    }

    fun leftBumperToggle(): Boolean {
        return leftBumper() && !leftBumperPrev
    }

    fun rightBumperToggle(): Boolean {
        return rightBumper() && !rightBumperPrev
    }

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
