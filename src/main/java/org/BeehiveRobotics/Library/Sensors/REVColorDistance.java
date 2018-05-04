package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class REVColorDistance {
	private ColorSensor colorSensor;
	private DistanceSensor distanceSensor;

	public REVColorDistance(LinearOpMode opMode, String colorName, String distanceName) {
		HardwareMap hardwareMap = opMode.hardwareMap;
		colorSensor = hardwareMap.get(ColorSensor.class, colorName);
		distanceSensor = hardwareMap.get(DistanceSensor.class, distanceName);
	}

	public ColorSensor getColorSensor() {
		return colorSensor;
	}
	public DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

	public double getDistance(DistanceUnit distanceUnit) {
		return distanceSensor.getDistance(distanceUnit);
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
