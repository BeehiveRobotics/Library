package org.BeehiveRobotics.Library.Motors

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.BeehiveRobotics.Library.Util.BROpMode


class KTTankDrive(opMode: BROpMode, gearedType: GearedType) : Runnable {
    private var opMode: BROpMode = opMode
    private var gearedType: GearedType = gearedType
    private val hardwareMap: HardwareMap = opMode.hardwareMap
    private val FrontLeft: KTMotor = KTMotor(opMode, "fl")
    private val FrontRight: KTMotor = KTMotor(opMode, "fr")
    private val RearLeft: KTMotor = KTMotor(opMode, "rl")
    private val RearRight: KTMotor = KTMotor(opMode, "rr")
    private var CPR: Double = 0.toDouble() //Clicks per rotation of each motor
    private var RPM: Double = 0.toDouble() //Rotations per Minute of each motor
    private var WD: Double = 0.toDouble()  //Wheel diameter
    private lateinit var model: MotorModel //Which model of motor is being used
    private var target: Double = 0.toDouble() //target for the motors to move to (in clicks)
    private var MIN_SPEED = 0.25
    private var MAX_SPEED = 1.0
    private var leftSpeed: Double = 0.toDouble()
    private var rightSpeed: Double = 0.toDouble()
    private var isBusy = false

    fun setMinSpeed(speed: Double): KTTankDrive {
        this.MIN_SPEED = speed
        FrontLeft.setMinSpeed(MIN_SPEED)
        FrontRight.setMinSpeed(MIN_SPEED)
        RearLeft.setMinSpeed(MIN_SPEED)
        RearRight.setMinSpeed(MIN_SPEED)
        return this
    }

    fun setMaxSpeed(speed: Double): KTTankDrive {
        this.MAX_SPEED = speed
        FrontLeft.setMaxSpeed(MAX_SPEED)
        FrontRight.setMaxSpeed(MAX_SPEED)
        RearLeft.setMaxSpeed(MAX_SPEED)
        RearRight.setMaxSpeed(MAX_SPEED)
        return this
    }

    enum class GearedType {
        NORMAL, REVERSED
    }

    constructor(opMode: BROpMode) : this(opMode, GearedType.NORMAL)

    fun init() {
        if (gearedType == GearedType.NORMAL) {
            FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE)
            RearLeft.setDirection(DcMotorSimple.Direction.REVERSE)
            FrontRight.setDirection(DcMotorSimple.Direction.FORWARD)
            RearRight.setDirection(DcMotorSimple.Direction.FORWARD)
        } else {
            FrontRight.setDirection(DcMotorSimple.Direction.REVERSE)
            RearRight.setDirection(DcMotorSimple.Direction.REVERSE)
            FrontLeft.setDirection(DcMotorSimple.Direction.FORWARD)
            RearLeft.setDirection(DcMotorSimple.Direction.FORWARD)
        }
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        resetEncoders()
        setModel(MotorModel.NEVEREST40)
        WD = 3.937
        setMinSpeed(MIN_SPEED)
    }

    /*
    A way to reset the encoders of each motor
     */
    private fun resetEncoders(): KTTankDrive {
        FrontLeft.resetEncoder()
        FrontRight.resetEncoder()
        RearLeft.resetEncoder()
        RearRight.resetEncoder()
        return this
    }

    /*
    Quick way to set float or brake for each motor
     */
    private fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior): KTTankDrive {
        FrontLeft.setZeroPowerBehavior(zeroPowerBehavior)
        FrontRight.setZeroPowerBehavior(zeroPowerBehavior)
        RearLeft.setZeroPowerBehavior(zeroPowerBehavior)
        RearRight.setZeroPowerBehavior(zeroPowerBehavior)
        return this
    }

    /*
    Quick way to set RunModes of each motor.
     */
    private fun setRunMode(runMode: DcMotor.RunMode): KTTankDrive {
        FrontLeft.setRunMode(runMode)
        FrontRight.setRunMode(runMode)
        RearLeft.setRunMode(runMode)
        RearRight.setRunMode(runMode)
        return this
    }

    fun getGearedType(): GearedType {
        return gearedType
    }

    fun setGearedType(gearedType: GearedType): KTTankDrive {
        this.gearedType = gearedType
        return this
    }

    /*
    Returns which model of motor is being used
     */
    private fun getModel(): MotorModel {
        return model
    }

    /*
    This is used to specify which model of motor is being used, so it can call things like clicks per rotation, and rotations per minute
     */
    private fun setModel(model: MotorModel): KTTankDrive {
        FrontLeft.setModel(model)
        FrontRight.setModel(model)
        RearLeft.setModel(model)
        RearRight.setModel(model)
        this.model = model
        this.RPM = MotorModel.RPM(model).toDouble()
        this.CPR = MotorModel.CPR(model)
        return this
    }

    /*
    This method is used for updating powers of motors. It automatically ramps based on targets set in setTarget()
     */
    private fun setPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setPower(fl)
        FrontRight.setPower(fr)
        RearLeft.setPower(rl)
        RearRight.setPower(rr)
    }

    /*
    This method is used for directly setting the power of the motors, such as for a TeleOp
     */
    private fun setRawPowers(fl: Double, fr: Double, rl: Double, rr: Double) {
        FrontLeft.setRawPower(fl)
        FrontRight.setRawPower(fr)
        RearLeft.setRawPower(rl)
        RearRight.setRawPower(rr)
    }

    /*
    This method is used to set the target variables for each of the motors
     */
    private fun setTarget(target: Double) {
        FrontLeft.setTarget(target)
        FrontRight.setTarget(target)
        RearLeft.setTarget(target)
        RearRight.setTarget(target)
        this.target = target
    }

    /*
    This method is used to convert inches of movement to clicks of an encoder
     */
    private fun inches_to_clicks(inches: Double): Double {
        val circumference = WD * Math.PI
        return CPR / circumference * inches
    }

    /*
    Method to drive. Takes in speed of the left side, speed of the right, and the inches to move
     */
    private fun drive(leftSpeed: Double, rightSpeed: Double, inches: Double) {
        this.drive(leftSpeed, rightSpeed, inches, true)
    }

    private fun drive(leftSpeed: Double, rightSpeed: Double, inches: Double, waitForCompletion: Boolean) {
        this.leftSpeed = leftSpeed
        this.rightSpeed = rightSpeed
        resetEncoders()
        val clicks = inches_to_clicks(inches)
        setTarget(clicks)
        if (waitForCompletion) {
            while (!(FrontLeft.isAtTarget() && FrontRight.isAtTarget() && RearLeft.isAtTarget() && RearRight.isAtTarget())) {
                setPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed)
                if (!opMode.opModeIsActive()) {
                    stopMotors()
                    isBusy = false
                }
            }
        } else {
            val thread = Thread(this)
            thread.start()
        }
        stopMotors()
        isBusy = false
    }

    fun drive(leftSpeed: Double, rightSpeed: Double) {
        setRawPowers(leftSpeed, rightSpeed, leftSpeed, rightSpeed)
    }

    /*
    Stops the motors
     */
    fun stopMotors() {
        FrontLeft.stopMotor()
        FrontRight.stopMotor()
        RearLeft.stopMotor()
        RearRight.stopMotor()
    }

    fun forward(speed: Double, inches: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, speed, inches)
    }

    fun backward(speed: Double, inches: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, speed, inches)
    }

    fun spinRight(speed: Double, inches: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, -speed, inches)
    }

    fun spinLeft(speed: Double, inches: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, -speed, inches)
    }

    fun leftForward(speed: Double, inches: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, 0.0, inches)
    }

    fun leftBackward(speed: Double, inches: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, 0.0, inches)
    }

    fun rightForward(speed: Double, inches: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(0.0, speed, inches)
    }

    fun rightBackward(speed: Double, inches: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(0.0, speed, inches)
    }

    fun forward(speed: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, speed)
    }

    fun backward(speed: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, speed)
    }

    fun spinRight(speed: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, -speed)
    }

    fun spinLeft(speed: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, -speed)
    }

    fun leftForward(speed: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(speed, 0.0)
    }

    fun leftBackward(speed: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(speed, 0.0)
    }

    fun rightForward(speed: Double) {
        var speed = speed
        speed = Math.abs(speed)
        drive(0.0, speed)
    }

    fun rightBackward(speed: Double) {
        var speed = speed
        speed = -Math.abs(speed)
        drive(0.0, speed)
    }

    override fun run() {
        val flThread = Thread(FrontLeft)
        val frThread = Thread(FrontRight)
        val rlThread = Thread(RearLeft)
        val rrThread = Thread(RearRight)
        flThread.start()
        frThread.start()
        rlThread.start()
        rrThread.start()
    }

}