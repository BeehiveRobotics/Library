package org.BeehiveRobotics.Library.Motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import java.util.Runnable;

public class Motor implements Runnable {
    private static final double RAMP_LOG_EXPO = 0.8;
    private double MIN_SPEED = 0.2;
    private double MAX_SPEED = 1;
    private DcMotor motor;
    private MotorModel model;
    private String name;
    private LinearOpMode opMode;
    private double target;
    private double current;
    private double power;

    Motor(LinearOpMode opMode, String name) {
        this.name = name;
        this.opMode = opMode;
        this.motor = this.opMode.hardwareMap.get(DcMotor.class, name);
    }

    Motor setRunMode(DcMotor.RunMode runMode) {
        this.motor.setMode(runMode);
        return this;
    }

    Motor setModel(MotorModel motorModel) {
        this.model = motorModel;
        return this;
    }

    Motor setMinSpeed(double speed) {
        this.MIN_SPEED = Math.abs(speed);
        return this;
    }

    public Motor setMaxSpeed(double speed) {
        this.MAX_SPEED = Math.abs(speed);
        return this;
    }

    Motor resetEncoder() {
        DcMotor.RunMode initialBehavior = this.getRunMode();
        this.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.setRunMode(initialBehavior);
        return this;
    }

    double getCurrentPosition() {
        return this.motor.getCurrentPosition();
    }

    Motor setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        this.motor.setZeroPowerBehavior(zeroPowerBehavior);
        return this;
    }

    void setTarget(double target) {
        this.target = Math.abs(target);
    }
    
    void runToTarget(double target, double power, boolean waitForStop) {
        this.target = Math.abs(target);
        this.power = power;
        if(!waitForStop) {
            Thread thread = new Thread(this);
            thread.start();
        } else {
            while(!this.isAtTarget()) {
                setPower(power);
            }
        }
    }
    
    void runToTarget(double target, double power) {
        this.runToTarget(target, power, true);
    }

    void setRawPower(double power) {
        if (opMode.opModeIsActive()) {
            if (power > 0) {
                this.motor.setPower(Range.clip(power, MIN_SPEED, MAX_SPEED));
            } else if (power < 0) {
                this.motor.setPower(Range.clip(power, -MAX_SPEED, -MIN_SPEED));
            } else {
                stopMotor();
            }
        } else {
            stopMotor();
        }
    }

    void setPower(double power) {
        this.power = power;
        if (!(this.opMode.opModeIsActive()) || power == 0) {
            stopMotor();
            return;
        }
        this.current = getCurrentPosition();
        double k = 4 / target;
        double calculated_power = k * this.current * (1 - current / target) * power + Double.MIN_VALUE;
        double expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO);
        if (power < 0) {
            setRawPower(-expo_speed);
            return;
        }
        setRawPower(expo_speed);
    }

    void setPower(double power, double current, double target) {
        this.power = power;
        if (!(this.opMode.opModeIsActive()) || power == 0) {
            stopMotor();
            return;
        }
        this.current = current;
        this.target = target;
        double k = 4 / target;
        double calculated_power = k * this.current * (1 - current / target) * power + Double.MIN_VALUE;
        double expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO);
        if (power < 0) {
            setRawPower(-expo_speed);
            return;
        }
        setRawPower(expo_speed);
    }

    void stopMotor() {
        this.motor.setPower(0);
    }

    DcMotor.RunMode getRunMode() {
        return this.motor.getMode();
    }

    String getName() {
        return this.name;
    }

    boolean isAtTarget() {
        return Math.abs(current) >= Math.abs(target);
    }

    public Motor setDirection(DcMotorSimple.Direction direction) {
        this.motor.setDirection(direction);
        return this;

    }
    
    public void run() {
        while(!this.isAtTarget() && this.opModeIsActive()) {
            this.setPower(this.power);
        }
        stopMotor();
        Thread.currentThread().interrupt();
    }
}
