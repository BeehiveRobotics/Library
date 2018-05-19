package org.BeehiveRobotics.Library.Sensors;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

public class VuforiaCamera {
	public class ClosableVuforiaLocalizer extends VuforiaLocalizerImpl {
		private boolean closed = false;
		
		public ClosableVuforiaLocalizer(VuforiaLocalizer.Parameters parameters) {
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
	

