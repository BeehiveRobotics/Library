package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Util.BROpMode

abstract class HardwareDevice(val name: String, private val opMode: BROpMode): SubSystem(opMode) {

}