package org.BeehiveRobotics.Library.Util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class BROpMode extends LinearOpMode {
    public enum OpModeType {
        Autonomous, TeleOp
    }
    private OpModeType opModeType;
    public abstract void initialize();
    public abstract void run();
    public final void setOpModeType(OpModeType opModeType){
        this.opModeType = opModeType;
    }
    protected Controller controller1, controller2;
    public final void runOpMode() throws InterruptedException {
        try {
            controller1 = new Controller(gamepad1);
            controller2 = new Controller(gamepad2);
            initialize();
            waitForStart();
            if(opModeType == OpModeType.Autonomous) {
                run();
            }
            else {
                while(opModeIsActive()) {
                    run();
                }
            }
        } finally {
            end();
        }
    }
    public void end() {}
}
