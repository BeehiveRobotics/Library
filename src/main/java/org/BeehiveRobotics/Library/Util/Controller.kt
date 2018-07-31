package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.hardware.Gamepad

class Controller(private val gamepad: Gamepad) {
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
    
    var leftStickX = 0.0
        private set
        get() = gamepad.left_stick_x.toDouble()
    var leftStickY = 0.0
        private set
        get() = -gamepad.left_stick_y.toDouble()
    var rightStickX = 0.0
        private set
        get() = gamepad.right_stick_x.toDouble()
    var rightStickY = 0.0
        private set
        get() = -gamepad.right_stick_y.toDouble()

    var leftTrigger = 0.0
        private set
        get() = gamepad.left_trigger.toDouble()
    var rightTrigger = 0.0
        private set
        get() = gamepad.right_trigger.toDouble()

    var leftBumper = false
        private set
        get() = gamepad.left_bumper
    var rightBumper = false
        private set
        get() = gamepad.right_bumper

    var dpad_up = false
        private set
        get() = gamepad.dpad_up
    var dpad_down = false
        private set
        get() = gamepad.dpad_down
    var dpad_left = false
        private set
        get() = gamepad.dpad_left
    var dpad_right = false
        private set
        get() = gamepad.dpad_right

    var a = false
        private set
        get() = gamepad.a
    var b = false
        private set
        get() = gamepad.b
    var x = false
        private set
        get() = gamepad.x
    var y = false
        private set
        get() = gamepad.y

    var aToggle = false
        private set
        get() = a && !aPrev
    var bToggle = false
        private set
        get() = b && !bPrev
    var xToggle = false
        private set
        get() = x && !xPrev
    var yToggle = false
        private set
        get() = y && !yPrev

    var leftBumperToggle = false
        private set
        get() = leftBumper && !leftBumperPrev
    var rightBumperToggle = false
        private set
        get() = rightBumper && !rightBumperPrev

    var dpad_upToggle = false
        private set
        get() = dpad_up && !dpad_upPrev
    var dpad_downToggle = false
        private set
        get() = dpad_down && !dpad_downPrev
    var dpad_leftToggle = false
        private set
        get() = dpad_left && !dpad_leftPrev
    var dpad_rightToggle = false
        private set
        get() = dpad_right && !dpad_rightPrev

    fun update() {
        aPrev = a
        bPrev = b
        xPrev = x
        yPrev = y

        leftBumperPrev = leftBumper
        rightBumperPrev = rightBumper

        dpad_upPrev = dpad_up
        dpad_downPrev = dpad_down
        dpad_leftPrev = dpad_left
        dpad_rightPrev = dpad_right
    }
}
