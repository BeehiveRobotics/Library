package org.BeehiveRobotics.Library.Motors;

import android.os.NetworkOnMainThreadException;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.MissingFormatArgumentException;

public class TankDrive {
    private final LinearOpMode opMode;
    private GearedType gearedType;
    private Motor FrontLeft;
    private Motor FrontRight;
    private Motor RearLeft;
    private Motor RearRight;
    private double CPR; //Clicks per rotation of each motor
    private double RPM; //Rotations per Minute of each motor
    private double WD;  //Wheel diameter
    private MotorModel model; //Which model of motor is being used
    private double target; //target for the motors to move to (in clicks)
    private double MIN_SPEED = 0.25;
    private double MAX_SPEED = 1;

    public void setMinSpeed(double speed) {
        this.MIN_SPEED = speed;
        FrontLeft.setMinSpeed(MIN_SPEED);
        FrontRight.setMinSpeed(MIN_SPEED);
        RearLeft.setMinSpeed(MIN_SPEED);
        RearRight.setMinSpeed(MIN_SPEED);
    }

    public void setMaxSpeed(double speed) {
        this.MAX_SPEED = speed;
        FrontLeft.setMaxSpeed(MAX_SPEED);
        FrontRight.setMaxSpeed(MAX_SPEED);
        RearLeft.setMaxSpeed(MAX_SPEED);
        RearRight.setMaxSpeed(MAX_SPEED);
    }

    public enum GearedType {NORMAL, REVERSED}

    public TankDrive(LinearOpMode linearOpMode) {
        this(linearOpMode, GearedType.NORMAL);
    }

    public TankDrive(LinearOpMode linearOpMode, GearedType gearedType) {
        this.opMode = linearOpMode;
        this.gearedType = gearedType;
    }
    public void mapHardware() {
        FrontLeft = new Motor(opMode, "fl");
        FrontRight = new Motor(opMode, "fr");
        RearLeft = new Motor(opMode, "rl");
        RearRight = new Motor(opMode, "rr");
        if(gearedType == GearedType.NORMAL) {
            FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            RearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            FrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            RearRight.setDirection(DcMotorSimple.Direction.FORWARD);
        } else {
            FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
            RearRight.setDirection(DcMotorSimple.Direction.REVERSE);
            FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            RearLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        resetEncoders();
        setModel(MotorModel.NEVEREST40);
        WD = 3.937;
        setMinSpeed(MIN_SPEED);
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

    public GearedType getGearedType() {
        return gearedType;
    }

    public void setGearedType(GearedType gearedType) {
        this.gearedType = gearedType;
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
        this.model = model;
        this.RPM = MotorModel.RPM(model);
        this.CPR = MotorModel.CPR(model);
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
        return CPR / circumference * inches;
    }
    /*
    Method to drive. Takes in speed of the left side, speed of the right, and the inches to move
     */
    private void drive(double leftSpeed, double rightSpeed, double inches) {
        resetEncoders();
        double clicks = inches_to_clicks(inches);
        setTarget(clicks);
        setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
        while (!(FrontLeft.isAtTarget() && FrontRight.isAtTarget() && RearLeft.isAtTarget() && RearRight.isAtTarget())) {
            setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
        }
        stopMotors();
    }

    public void drive(double leftSpeed, double rightSpeed) {
        setRawPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
    }

    /*
    Stops the motors
     */
    public void stopMotors() {
        FrontLeft.stopMotor();
        FrontRight.stopMotor();
        RearLeft.stopMotor();
        RearRight.stopMotor();
    }

    public void forward(double speed, double inches) {
        speed = Math.abs(speed);
        drive(speed, speed, inches);
    }

    public void backward(double speed, double inches) {
        speed = -Math.abs(speed);
        drive(speed, speed, inches);
    }

    public void spinRight(double speed, double inches) {
        speed = Math.abs(speed);
        drive(speed, -speed, inches);
    }

    public void spinLeft(double speed, double inches) {
        speed = -Math.abs(speed);
        drive(speed, -speed, inches);
    }

    public void leftForward(double speed, double inches) {
        speed = Math.abs(speed);
        drive(speed, 0, inches);
    }

    public void leftBackward(double speed, double inches) {
        speed = -Math.abs(speed);
        drive(speed, 0, inches);
    }

    public void rightForward(double speed, double inches) {
        speed = Math.abs(speed);
        drive(0, speed, inches);
    }

    public void rightBackward(double speed, double inches) {
        speed = -Math.abs(speed);
        drive(0, speed, inches);
    }

    public void forward(double speed) {
        speed = Math.abs(speed);
        drive(speed, speed);
    }

    public void backward(double speed) {
        speed = -Math.abs(speed);
        drive(speed, speed);
    }

    public void spinRight(double speed) {
        speed = Math.abs(speed);
        drive(speed, -speed);
    }

    public void spinLeft(double speed) {
        speed = -Math.abs(speed);
        drive(speed, -speed);
    }

    public void leftForward(double speed) {
        speed = Math.abs(speed);
        drive(speed, 0);
    }

    public void leftBackward(double speed) {
        speed = -Math.abs(speed);
        drive(speed, 0);
    }

    public void rightForward(double speed) {
        speed = Math.abs(speed);
        drive(0, speed);
    }

    public void rightBackward(double speed) {
        speed = -Math.abs(speed);
        drive(0, speed);
    }

}
