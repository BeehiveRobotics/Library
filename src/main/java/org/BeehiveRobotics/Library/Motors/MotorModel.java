package org.BeehiveRobotics.Library.Motors;

public enum MotorModel {
    NEVEREST20, NEVEREST40, NEVEREST60;
    public double CPR;
    public double DEFAULT_CPR = 0;
    public double CPR(MotorModel motorModel) {
        switch (motorModel){
            case NEVEREST20:
                return 537.6;
            case NEVEREST40:
                return 1120;
            case NEVEREST60:
                return 1680;
        }
        return 1000;
    }
    public int RPM;
    public int DEFAULT_RPM = 0;
    public int RPM(MotorModel motorModel) {
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
