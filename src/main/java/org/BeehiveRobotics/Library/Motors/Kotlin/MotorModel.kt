package org.BeehiveRobotics.Library.Motors.Kotlin

enum class MotorModel {
    NEVEREST20, NEVEREST40, NEVEREST60;

    var CPR: Double = 0.toDouble()
    var DEFAULT_CPR = 0.0
    fun CPR(motorModel: MotorModel): Double {
        when (motorModel) {
            NEVEREST20 -> return 537.6
            NEVEREST40 -> return 1120.0
            NEVEREST60 -> return 1680.0
        }
        return DEFAULT_CPR
    }

    var RPM: Int = 0
    var DEFAULT_RPM = 0
    fun RPM(motorModel: MotorModel): Int {
        when (motorModel) {
            NEVEREST20 -> return 340
            NEVEREST40 -> return 160
            NEVEREST60 -> return 105
        }
        return DEFAULT_RPM
    }

}