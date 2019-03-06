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

    private var DEBOUNCING_NUMBER_OF_SAMPLES = 10
    private var aCountOn = 0
    private var bCountOn = 0
    private var xCountOn = 0
    private var yCountOn = 0
    private var leftBumperCountOn = 0
    private var rightBumperCountOn = 0
    private var dpadUpCountOn = 0
    private var dpadDownCountOn = 0
    private var dpadLeftCountOn = 0
    private var dpadRightCountOn = 0

    val leftStickX: Double
        get() = gamepad.left_stick_x.toDouble()
    val leftStickY: Double
        get() = -gamepad.left_stick_y.toDouble()
    val rightStickX: Double
        get() = gamepad.right_stick_x.toDouble()
    val rightStickY: Double
        get() = -gamepad.right_stick_y.toDouble()

    val leftTrigger: Double
        get() = gamepad.left_trigger.toDouble()
    val rightTrigger: Double
        get() = gamepad.right_trigger.toDouble()

    val leftBumper: Boolean
        get() = gamepad.left_bumper
    val rightBumper: Boolean
        get() = gamepad.right_bumper

    val dpadUp: Boolean
        get() = gamepad.dpad_up
    val dpadDown: Boolean
        get() = gamepad.dpad_down
    val dpadLeft: Boolean
        get() = gamepad.dpad_left
    val dpadRight: Boolean
        get() = gamepad.dpad_right

    val a: Boolean
        get() = gamepad.a
    val b: Boolean
        get() = gamepad.b
    val x: Boolean
        get() = gamepad.x
    val y: Boolean
        get() = gamepad.y

    val aToggle: Boolean
        get() = aCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val bToggle: Boolean
        get() = bCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val xToggle: Boolean
        get() = xCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val yToggle: Boolean
        get() = yCountOn == DEBOUNCING_NUMBER_OF_SAMPLES

    val leftBumperToggle: Boolean
        get() = leftBumperCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val rightBumperToggle: Boolean
        get() = rightBumperCountOn == DEBOUNCING_NUMBER_OF_SAMPLES

    val dpadUpToggle: Boolean
        get() = dpadUpCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val dpadDownToggle: Boolean
        get() = dpadDownCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val dpadLeftToggle: Boolean
        get() = dpadLeftCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    val dpadRightToggle: Boolean
        get() = dpadRightCountOn == DEBOUNCING_NUMBER_OF_SAMPLES

    fun update() {
        if(a) aCountOn++
        else aCountOn = 0
        if(b) bCountOn++
        else bCountOn = 0
        if(x) xCountOn++
        else xCountOn = 0
        if(y) yCountOn++
        else yCountOn = 0

        if(leftBumper) leftBumperCountOn++
        else leftBumperCountOn = 0
        if(rightBumper) rightBumperCountOn++
        else rightBumperCountOn = 0

        if(dpadUp) dpadUpCountOn++
        else dpadUpCountOn = 0
        if(dpadDown) dpadDownCountOn++
        else dpadDownCountOn = 0
        if(dpadLeft) dpadLeftCountOn++
        else dpadLeftCountOn = 0
        if(dpadRight) dpadRightCountOn++
        else dpadRightCountOn = 0
    }
}
