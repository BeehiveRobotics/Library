package org.BeehiveRobotics.Library.Motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TankDrive {

    private Motor FrontLeft;
    private Motor FrontRight;
    private Motor RearLeft;
    private Motor RearRight;
    private LinearOpMode opMode;

    public TankDrive(LinearOpMode linearOpMode) {
        this.opMode = linearOpMode;
        FrontLeft = new Motor(opMode, "fl");
        FrontRight = new Motor(opMode, "fr");
        RearLeft = new Motor(opMode, "rl");
        RearRight = new Motor(opMode, "rr");
    }
    private void setPowers(double fl, double fr, double rl, double rr) {
        FrontLeft.setPower(fl);
        FrontRight.setPower(fr);
        RearLeft.setPower(rl);
        RearRight.setPower(rr);
    }
}
