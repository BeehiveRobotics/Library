package org.BeehiveRobotics.Library.Systems

import org.BeehiveRobotics.Library.Util.BROpMode
import org.BeehiveRobotics.Library.Robots.Robot

abstract class RobotSystem(private val opMode: BROpMode): Robot(opMode), Runnable {}