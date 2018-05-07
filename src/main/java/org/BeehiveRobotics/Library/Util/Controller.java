package org.BeehiveRobotics.Library.Util;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    private Gamepad gamepad;
    private boolean aPrev = false, bPrev = false, xPrev = false, yPrev = false, leftBumperPrev = false, rightBumperPrev = false, dpad_upPrev = false, dpad_downPrev = false, dpad_leftPrev = false, dpad_rightPrev = false;
    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public double leftStickX() {return gamepad.left_stick_x;}
    public double leftStickY() {return -gamepad.left_stick_y;}
    public double rightStickX() {return gamepad.right_stick_x;}
    public double rightStickY() {return -gamepad.right_stick_y;}

    public double leftTrigger() {return gamepad.left_trigger;}
    public double rightTrigger() {return gamepad.right_trigger;}

    public boolean leftBumper() {return gamepad.left_bumper;}
    public boolean rightBumper() {return gamepad.right_bumper;}

    public boolean dpad_up() {return gamepad.dpad_up;}
    public boolean dpad_down() {return gamepad.dpad_down;}
    public boolean dpad_left() {return gamepad.dpad_left;}
    public boolean dpad_right() {return gamepad.dpad_right;}

    public boolean a() {return gamepad.a;}
    public boolean b() {return gamepad.b;}
    public boolean x() {return gamepad.x;}
    public boolean y() {return gamepad.y;}

    public boolean aToggle() {return a() && !aPrev;}
    public boolean bToggle() {return b() && !bPrev;}
    public boolean xToggle() {return x() && !xPrev;}
    public boolean yToggle() {return y() && !yPrev;}

    public boolean leftBumperToggle() {return leftBumper() && !leftBumperPrev;}
    public boolean rightBumperToggle() {return rightBumper() && !rightBumperPrev;}

    public void update() {
        aPrev = a();
        bPrev = b();
        xPrev = x();
        yPrev = y();

        leftBumperPrev = leftBumper();
        rightBumperPrev = rightBumper();

        dpad_upPrev = dpad_up();
        dpad_downPrev = dpad_down();
        dpad_leftPrev = dpad_left();
        dpad_rightPrev = dpad_right();
    }

    public Gamepad getGamepad() {
        return gamepad;
    }
}
