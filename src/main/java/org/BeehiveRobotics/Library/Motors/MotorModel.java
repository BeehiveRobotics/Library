package org.BeehiveRobotics.Library.Motors;

public enum MotorModel {
    NEVEREST20, NEVEREST40, NEVEREST60;
    public double CPR;
    public static double DEFAULT_CPR = 0;
    public static double CPR(MotorModel motorModel) {
        switch (motorModel){
            case NEVEREST20:
                return 537.6;
            case NEVEREST40:
                return 1120;
            case NEVEREST60:
                return 1680;
        }
        return DEFAULT_CPR;
    }
    public int RPM;
    public static int DEFAULT_RPM = 0;
    public static int RPM(MotorModel motorModel) {
        switch (motorModel) {
            case NEVEREST20:
                return 340;
            case NEVEREST40:
                return 160;
            case NEVEREST60:
                return 105;
        }
        return DEFAULT_RPM;
    }
}
