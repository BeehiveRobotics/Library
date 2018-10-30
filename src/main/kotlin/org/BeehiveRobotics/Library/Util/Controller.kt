package org.BeehiveRobotics.Library.Util

import com.qualcomm.robotcore.hardware.Gamepad

class Controller(private val gamepad: Gamepad) {
    private var aPrev = false
    private var bPrev = false
    private var xPrev = false
    private var yPrev = false
    private var leftBumperPrev = false
    private var rightBumperPrev = false
    private var dpadUpPrev = false
    private var dpadDownPrev = false
    private var dpadLeftPrev = false
    private var dpadRightPrev = false
    
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

    var dpadUp = false
        private set
        get() = gamepad.dpad_up
    var dpadDown = false
        private set
        get() = gamepad.dpad_down
    var dpadLeft = false
        private set
        get() = gamepad.dpad_left
    var dpadRight = false
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

    var dpadUpToggle = false
        private set
        get() = dpadUp && !dpadUpPrev
    var dpadDownToggle = false
        private set
        get() = dpadDown && !dpadDownPrev
    var dpadLeftToggle = false
        private set
        get() = dpadLeft && !dpadLeftPrev
    var dpadRightToggle = false
        private set
        get() = dpadRight && !dpadRightPrev

    fun update() {
        aPrev = a
        bPrev = b
        xPrev = x
        yPrev = y

        leftBumperPrev = leftBumper
        rightBumperPrev = rightBumper

        dpadUpPrev = dpadUp
        dpadDownPrev = dpadDown
        dpadLeftPrev = dpadLeft
        dpadRightPrev = dpadRight
    }
}
