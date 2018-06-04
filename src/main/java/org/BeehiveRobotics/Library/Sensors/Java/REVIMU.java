package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class REVIMU {
    private HardwareMap hardwareMap;
    private BNO055IMU imu;
    private BNO055IMU.Parameters parameters;
    private boolean isInitialized = false;
    private double heading;
    public REVIMU (OpMode opMode) {
        this.hardwareMap = opMode.hardwareMap;
        this.parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
    }
    private void init() {isInitialized = imu.initialize(parameters);}
    public void calibrate() {
        ElapsedTime time = new ElapsedTime();
        time.reset();
        while(!isInitialized) {
            if(time.seconds()>10) {
                break;
            }
            init();
        }
        heading = getHeading();
    }

    public double getHeading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public BNO055IMU.Parameters getParameters() { 
    	return this.parameters;
    }

    public boolean getInitialized() {
	    return this.isInitialized;
    }

    public BNO055IMU getIMU() {
	    return this.imu;
    }


}