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

    private var DEBOUNCING_NUMBER_OF_SAMPLES = 20
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
        get() = aCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var bToggle = false
        private set
        get() = bCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var xToggle = false
        private set
        get() = xCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var yToggle = false
        private set
        get() = yCountOn == DEBOUNCING_NUMBER_OF_SAMPLES

    var leftBumperToggle = false
        private set
        get() = leftBumperCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var rightBumperToggle = false
        private set
        get() = rightBumperCountOn == DEBOUNCING_NUMBER_OF_SAMPLES

    var dpadUpToggle = false
        private set
        get() = dpadUpCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var dpadDownToggle = false
        private set
        get() = dpadDownCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var dpadLeftToggle = false
        private set
        get() = dpadLeftCountOn == DEBOUNCING_NUMBER_OF_SAMPLES
    var dpadRightToggle = false
        private set
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
        
        
        /*
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
        */
    }
}
