package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class REVColorDistance {
<<<<<<< HEAD
	private ColorSensor csen;
	private DistanceSensor dsen;
	private DistanceUnit dunit;
=======
	private ColorSensor colorSensor;
	private DistanceSensor distanceSensor;
>>>>>>> 83df944289f7893a362304b82c4d3b5fdcafc40c

	public REVColorDistance(LinearOpMode opMode, String colorName, String distanceName) {
		HardwareMap hardwareMap = opMode.hardwareMap;
		colorSensor = hardwareMap.get(ColorSensor.class, colorName);
		distanceSensor = hardwareMap.get(DistanceSensor.class, distanceName);
	}

	public REVColorDistance setUnit(DistanceUnit unit) {
		dunit = unit;
		return this;
	}

	public ColorSensor getColorSensor() {
		return colorSensor;
	}
	public DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

<<<<<<< HEAD
	public double getDistance() {
		return dsen.getDistance(dunit);
=======
	public double getDistance(DistanceUnit distanceUnit) {
		return distanceSensor.getDistance(distanceUnit);
>>>>>>> 83df944289f7893a362304b82c4d3b5fdcafc40c
	}

	public double getRed() {
		return colorSensor.red();
	}

	public double getBlue() {
		return colorSensor.blue();
	}
	
	public double getGreen() {
		return colorSensor.green();
	}
	
	public double getAlpha() {
		return colorSensor.alpha();
	}

}
