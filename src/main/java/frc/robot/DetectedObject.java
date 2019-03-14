package frc.robot;


public class DetectedObject
{
    public String typeOfOjbect; //e.g. "LeftTarget"
    public double x;
    public double y;
    public double theta;  //yaw angle
    public long timeOfDetection;  //The System.currentTimeMilliseconds when the object was first reported
}