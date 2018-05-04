package org.BeehiveRobotics.Library.Motors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Motor {
    private static final double RAMP_LOG_EXPO = 0.8;
    private DcMotor motor;
    private MotorModel model;
    private String name;
    private LinearOpMode opMode;
    private double target;
    private double current;

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
        this.target = target;
    }

    void setPower(double power) {
        if (!(this.opMode.opModeIsActive()) || power == 0) {
            stopMotor();
            return;
        }
        this.current = getCurrentPosition();
        double k = 4 / target;
        double calculated_power = k * this.current * (1 - current / target) * power;
        double expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO);
        if (power < 0) {
            this.motor.setPower(-expo_speed);
            return;
        }
        this.motor.setPower(expo_speed);
    }
    void setPower(double power, double current, double target) {
        if (!(this.opMode.opModeIsActive()) || power == 0) {
            stopMotor();
            return;
        }
        double k = 4 / target;
        double calculated_power = k * this.current * (1 - current / target) * power;
        double expo_speed = Math.pow(Math.abs(calculated_power), RAMP_LOG_EXPO);
        if (power < 0) {
            this.motor.setPower(-expo_speed);
            return;
        }
        this.motor.setPower(expo_speed);
    }

    void stopMotor() {
        this.motor.setPower(0);
    }

    private DcMotor.RunMode getRunMode() {
        return this.motor.getMode();
    }

    private String getName() {
        return this.name;
    }

    boolean isAtTarget() {
        if (target < 0) {
            return current <= target;
        } else if (target > 0) {
            return current >= target;
        }
        return true;
    }
}