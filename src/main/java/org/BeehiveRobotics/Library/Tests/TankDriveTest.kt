package org.BeehiveRobotics.Library.Tests

import org.BeehiveRobotics.Library.Robots.TankRobot
import org.BeehiveRobotics.Library.Util.BROpMode
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
@Autonomous(name="Testing TankDrive", group = "Test")
class TankDriveTest: BROpMode() {
    private val robot: TankRobot = TankRobot(this)
    override fun initialize() {
        setOpModeType(OpModeType.Autonomous)
        this.robot.init()
    }
    override fun run() {
        showLine("Forward")
        this.robot.drive.forward(1.0, 12.0)
        showLine("Backward")
        this.robot.drive.backward(1.0, 12.0)
        showLine("Spin Right")
        this.robot.drive.spinRight(1.0, 12.0)
        showLine("Spin Left")
        this.robot.drive.spinLeft(1.0, 12.0)
        showLine("Left Forward")
        this.robot.drive.leftForward(1.0, 12.0)
        showLine("Left Backward")
        this.robot.drive.leftBackward(1.0, 12.0)
        showLine("Right Forward")
        this.robot.drive.rightForward(1.0, 12.0)
        showLine("Right Backward")
        this.robot.drive.rightBackward(1.0, 12.0)

        showLine("Forward")
        this.robot.drive.forward(1.0, 6.0)
        showLine("Backward")
        this.robot.drive.backward(1.0, 6.0)
        showLine("Spin Right")
        this.robot.drive.spinRight(1.0, 6.0)
        showLine("Spin Left")
        this.robot.drive.spinLeft(1.0, 6.0)
        showLine("Left Forward")
        this.robot.drive.leftForward(1.0, 6.0)
        showLine("Left Backward")
        this.robot.drive.leftBackward(1.0, 6.0)
        showLine("Right Forward")
        this.robot.drive.rightForward(1.0, 6.0)
        showLine("Right Backward")
        this.robot.drive.rightBackward(1.0, 6.0)

        showLine("Forward")
        this.robot.drive.forward(0.5, 12.0)
        showLine("Backward")
        this.robot.drive.backward(0.5, 12.0)
        showLine("Spin Right")
        this.robot.drive.spinRight(0.5, 12.0)
        showLine("Spin Left")
        this.robot.drive.spinLeft(0.5, 12.0)
        showLine("Left Forward")
        this.robot.drive.leftForward(0.5, 12.0)
        showLine("Left Backward")
        this.robot.drive.leftBackward(0.5, 12.0)
        showLine("Right Forward")
        this.robot.drive.rightForward(0.5, 12.0)
        showLine("Right Backward")
        this.robot.drive.rightBackward(0.5, 12.0)

    }
    override fun end() {
        this.robot.drive.stopMotors()
    }
}