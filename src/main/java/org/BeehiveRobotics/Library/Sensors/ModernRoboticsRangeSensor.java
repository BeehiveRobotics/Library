package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ModernRoboticsRangeSensor {
	private ModernRoboticsI2cRangeSensor rangeSensor;
	private DistanceUnit units;

	public ModernRoboticsRangeSensor(LinearOpMode opMode, String rangeName) {
		//units in this will automatically default to inches
		units = DistanceUnit.INCH;
		HardwareMap hwMap = opMode.hardwareMap;
		rangeSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, rangeName);
	}

	public ModernRoboticsRangeSensor setUnit(DistanceUnit unit) {
		units = unit;
		return this;
	}

	public double getRawUltrasonic() {
		return rangeSensor.rawUltrasonic();
	}

	public double getRawOptical() {
		return rangeSensor.rawOptical();
	}
	
	public double getCMOptical() {
		return rangeSensor.cmOptical();
	}

	public double getDistance() { //this will get the distance in whatever units you specify lol
		return rangeSensor.getDistance(units);
	}
	
	public ModernRoboticsI2cRangeSensor getSensor() {
		return this.rangeSensor;
	}
}
