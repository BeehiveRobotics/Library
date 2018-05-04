package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class REVColorDistance {
	private ColorSensor csen;
	private DistanceSensor dsen;

	public REVColorDistance(OpMode opMode, String colorName, String distanceName) {
		HardwareMap hw_map = opMode.hardwareMap;
		csen = hw_map.get(ColorSensor.class, colorName);
		dsen = hw_map.get(DistanceSensor.class, distanceName);
	}

	public ColorSensor getColorSensor() {
		return csen;
	}
	public DistanceSensor getDistanceSensor() {
		return dsen;
	}

	public double getDistance(DistanceUnit dunit) {
		return dsen.getDistance(dunit);
	}

	public double getRed() {
		return csen.red();
	}

	public double getBlue() {
		return csen.blue();
	}
	
	public double getGreen() {
		return csen.green();
	}
	
	public double getAlpha() {
		return csen.alpha();
	}

}
