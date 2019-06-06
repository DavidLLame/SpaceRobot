package frc.robot;

/**
 * This class is a temporary class containg the functions 
 * necessary to track a cargo ball and pick it up.
 */
public class BallChaser
{

    private Drive drive;
    private Manip manip;
    private ElevatorOps elevatorOps;


    //Coordinates of the ball, by whatever means it was detected
    //expressed in robot centered coordinates
    private double ballX; 
    private double ballY;
    private double ballTheta;
/*
    public void updatePosition(DetectedObject ballObservation)
    {
        ballX=ballObservation.x;
        ballY=ballObservation.y;
        ballTheta=ballObservation.theta;
    }

    public void followBall()
    {

        if (targetInRange())
        {
            drive.driveRaw(0,PICKUPSPEED, 0);
        }
        else
        {
            drive.driveByCamera(ballX, ballY, ballTheta);
        }
    }

*/


}