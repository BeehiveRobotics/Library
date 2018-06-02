package org.BeehiveRobotics.Library.Util.Java;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class BROpMode extends LinearOpMode {
    public enum OpModeType {
        Autonomous, TeleOp
    }

    private OpModeType opModeType = OpModeType.Autonomous;

    public abstract void initialize();

    public abstract void run();

    public abstract void end();

    protected final void setOpModeType(OpModeType opModeType) {
        this.opModeType = opModeType;
    }

    public final void addLine(String line) {
        telemetry.addLine(line);
        telemetry.update();
    }

    public final void addData(String title, String data) {
        telemetry.addData(title, data);
        telemetry.update();
    }

    protected Controller controller1;
    protected Controller controller2;

    public final void runOpMode() throws InterruptedException {
        try {
            controller1 = new Controller(gamepad1);
            controller2 = new Controller(gamepad2);
            initialize();
            waitForStart();
            switch (opModeType) {
                case TeleOp:
                    while (opModeIsActive()) {
                        run();
                        controller1.update();
                        controller2.update();
                    }
                case Autonomous:
                    run();
            }
        } finally {
            end();
        }
    }
}
