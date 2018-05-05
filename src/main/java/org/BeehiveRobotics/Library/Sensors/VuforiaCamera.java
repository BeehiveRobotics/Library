package org.BeehiveRobotics.Library.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;
import com.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import java.lang.Exception;

public class VuforiaCamera {
	//I don't believe in calling this class "Phone"
	//You shouldn't either.
	
	public class ClosableVuforiaLocalizer extends VuforiaLocalizerImpl {
		private boolean closed = false;
		
		public ClosableVuforiaLocalizer(Parameters parameters) {
			super(parameters);
		}

		@Override
		public void close() {
			if(closed == false) {
				super.close();
				closed = true;
			}
			else {
				//Throw some exception here.
				return;
			}
			
		}

		public boolean isClosed() {
			return closed;
		}
	}


	
}
	

