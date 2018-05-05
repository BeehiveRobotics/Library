package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ModernRoboticsRange {
	private ModernRoboticsI2cRangeSensor rangeSensor;
	private DistanceUnit units;

	public ModernRoboticsRange(LinearOpMode opMode, String rangeName) {
		//units in this will automatically default to cm
		units = DistanceUnit.cm;
		HardwareMap hwMap = opMode.hardwareMap;
		rangeSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, rangeName);
	}

	public ModernRoboticsRange setUnit(DistanceUnit unit) {
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
