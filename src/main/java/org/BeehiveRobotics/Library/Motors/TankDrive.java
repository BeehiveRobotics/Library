package org.BeehiveRobotics.Library.Motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDrive {
    private final LinearOpMode opMode;
    private final Motor FrontLeft;
    private final Motor FrontRight;
    private final Motor RearLeft;
    private final Motor RearRight;
    private double CPR; //Clicks per rotation of each motor
    private double RPM; //Rotations per Minute of each motor
    private double WD;  //Wheel diameter
    private MotorModel model; //Which model of motor is being used
    private double target; //target for the motors to move to (in clicks)

    public TankDrive(LinearOpMode linearOpMode) {
        this.opMode = linearOpMode;
        FrontLeft = new Motor(opMode, "fl");
        FrontRight = new Motor(opMode, "fr");
        RearLeft = new Motor(opMode, "rl");
        RearRight = new Motor(opMode, "rr");
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        resetEncoders();
        setModel(MotorModel.NEVEREST40);
        CPR = getModel().CPR;
        RPM = getModel().RPM;
        WD = 3.937;
    }

    private void resetEncoders() {
        FrontLeft.resetEncoder();
        FrontRight.resetEncoder();
        RearLeft.resetEncoder();
        RearRight.resetEncoder();
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior);
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior);
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior);
        RearRight.setZeroPowerBehavior(zeroPowerBehavior);
    }

    private void setRunMode(DcMotor.RunMode runMode) {
        FrontLeft.setRunMode(runMode);
        FrontRight.setRunMode(runMode);
        RearLeft.setRunMode(runMode);
        RearRight.setRunMode(runMode);
    }

    private MotorModel getModel() {
        return model;
    }

    private void setModel(MotorModel model) {
        FrontLeft.setModel(model);
        FrontRight.setModel(model);
        RearLeft.setModel(model);
        RearRight.setModel(model);
        RPM = model.RPM;
        CPR = model.CPR;
    }

    private void setPowers(double fl, double fr, double rl, double rr) {
        FrontLeft.setPower(fl);
        FrontRight.setPower(fr);
        RearLeft.setPower(rl);
        RearRight.setPower(rr);
    }

    private void setTarget(double target) {
        FrontLeft.setTarget(target);
        FrontRight.setTarget(target);
        RearLeft.setTarget(target);
        RearRight.setTarget(target);
        this.target = target;
    }
    private double inches_to_clicks(double inches) {
        double circumference = WD * Math.PI;
        return CPR / circumference;
    }
}
