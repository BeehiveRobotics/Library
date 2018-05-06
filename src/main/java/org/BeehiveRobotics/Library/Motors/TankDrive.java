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

    /*
    A way to reset the encoders of each motor
     */
    private void resetEncoders() {
        FrontLeft.resetEncoder();
        FrontRight.resetEncoder();
        RearLeft.resetEncoder();
        RearRight.resetEncoder();
    }

    /*
    Quick way to set float or brake for each motor
     */
    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior);
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior);
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior);
        RearRight.setZeroPowerBehavior(zeroPowerBehavior);
    }

    /*
    Quick way to set RunModes of each motor.
     */
    private void setRunMode(DcMotor.RunMode runMode) {
        FrontLeft.setRunMode(runMode);
        FrontRight.setRunMode(runMode);
        RearLeft.setRunMode(runMode);
        RearRight.setRunMode(runMode);
    }

    /*
    Returns which model of motor is being used
     */
    private MotorModel getModel() {
        return model;
    }

    /*
    This is used to specify which model of motor is being used, so it can call things like clicks per rotation, and rotations per minute
     */
    private void setModel(MotorModel model) {
        FrontLeft.setModel(model);
        FrontRight.setModel(model);
        RearLeft.setModel(model);
        RearRight.setModel(model);
        RPM = model.RPM;
        CPR = model.CPR;
    }

    /*
    This method is used for updating powers of motors. It automatically ramps based on targets set in setTarget()
     */
    private void setPowers(double fl, double fr, double rl, double rr) {
        FrontLeft.setPower(fl);
        FrontRight.setPower(fr);
        RearLeft.setPower(rl);
        RearRight.setPower(rr);
    }

    /*
    This method is used for directly setting the power of the motors, such as for a TeleOp
     */
    private void setRawPowers(double fl, double fr, double rl, double rr) {
        FrontLeft.setRawPower(fl);
        FrontRight.setRawPower(fr);
        RearLeft.setRawPower(rl);
        RearRight.setRawPower(rr);
    }


    /*
    This method is used to set the target variables for each of the motors
     */
    private void setTarget(double target) {
        FrontLeft.setTarget(target);
        FrontRight.setTarget(target);
        RearLeft.setTarget(target);
        RearRight.setTarget(target);
        this.target = target;
    }

    /*
    This method is used to convert inches of movement to clicks of an encoder
     */
    private double inches_to_clicks(double inches) {
        double circumference = WD * Math.PI;
        return CPR / circumference;
    }

    /*
    Method to drive. Takes in speed of the left side, speed of the right, and the inches to move
     */
    private void drive(double leftSpeed, double rightSpeed, double inches) {
        resetEncoders();
        double clicks = inches_to_clicks(inches);
        setTarget(clicks);
        setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
        while(!(FrontLeft.isAtTarget() && FrontRight.isAtTarget() && RearLeft.isAtTarget() && RearRight.isAtTarget())) {
            setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
        }
        stopMotors();
    }

    private void stopMotors() {
        FrontLeft.stopMotor();
        FrontRight.stopMotor();
        RearLeft.stopMotor();
        RearRight.stopMotor();
    }
}
