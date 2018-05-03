package org.BeehiveRobotics.Library.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Motor {
	private DcMotor motor;
	private String name;
	private LinearOpMode opMode;

	public Motor(LinearOpMode opMode, String name) {
		this.name = name;
		this.opMode = opMode;
		this.opMode.hardwareMap.get(DcMotor.class, name);
	}

	public Motor setRunMode(DcMotor.RunMode runMode) {
		this.motor.setRunMode(runMode);
		return this.motor;
	}

	public Motor resetEncoder() {
		ZeroPowerBehavior initialBehavior = this.getRunMode();
		this.setRunMode(DcMotor.ZeroPowerBehavior.STOP_AND_RESET_ENCODER);
		this.setRunMode(initialBehavior);
		return this.motor;
	}
	public Motor setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		this.motor.setZeroPowerBehavior(zeroPowerBehavior);
		return this.motor;
	}
}