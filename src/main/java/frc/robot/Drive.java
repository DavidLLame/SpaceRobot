package frc.robot;

import javax.lang.model.util.ElementScanner6;

/**
 * The class for all driving related functions
 */
public class Drive{

    private final double AXISDEADBAND=0.1;
    private final double TWISTDEADBAND=0.1;
    private final double ROTATIONTHRESHOLD=10;//10 degrees per second.  Threshold to determine if rtation has stopped.
    private final double CORRECTIONCONSTANT=.01;//Kp of a rotation PID control

    public DriveCoordinates drivingMode=DriveCoordinates.FIELD_CENTERED;

    double deadbanded (double joyY, double deadband) {
        if ((Math.abs(joyY) >= deadband)) {
            return joyY;
        }
        else{
            return 0;
        }
    }


    //Notice that class names start with capital letters.
    //Members of the class (variables and methods) start with a lower case letter, but subsequent words have an upper case letter.
    //The initial lower case followed by upper case words is called "camel case"
    //constants are all upper case.
    //Those casing "rules" aren't requirements.  They're just conventions that most Java programmers use.
    //More people will be able to understand your code if you follow the same patterns.

    //Of course, conventions like those just happen when somebody writes a book saying it's a good idea,
    //and a lot of people use the book.  So, some day, someone else will write a different book and people
    //will start doing it the way that book says.  Also, those conventions are only standard for Java, not
    //for C#.  The C# programmers read different books.

    boolean overrideDriver=true;
    /**
     * This function is called to do "normal", teleperated, driving.
     */
    public void driveByJoystick()
    {if (overrideDriver)
        {
            System.out.println("Yaw: "+Io.navX.getYaw()+" Angle: "+ Io.navX.getAngle());
            double absspeed=0.5;
            double twistspeed=0.5;
            double VelX=0;
            double VelY=0;
            double VelTwist=0;

            if (Io.joystick.getRawButton(1))
            {//Drive forward
                VelY=absspeed;
                VelX=0;
                VelTwist=0;
            }
            else if (Io.joystick.getRawButton(2))
            {//Drive backward
                VelX=0;
                VelY=-absspeed;
                VelTwist=0;
            }
            else if (Io.joystick.getRawButton(3))
            { //Drive left
              VelX=-absspeed;
              VelY=0;
              VelTwist=0;
            }
            else if (Io.joystick.getRawButton(4))
            {
                //Drive right
                VelX=absspeed;
                VelY=0;
                VelTwist=0;
            }
            else if (Io.joystick.getRawButton(5))
            {
                //Rotate clockwise
                VelX=0;
                VelY=0;
                VelTwist=twistspeed;
            }
            else if (Io.joystick.getRawButton(6))
            {
                //Rotate clockwise while driving forward
                VelX=0;
                VelY=absspeed;
                VelTwist=twistspeed;
            }
            else if (Io.joystick.getRawButton(8))
            {
                //Drive forward and slight right while rotating to 30 degrees
                //Have to do some more work on signs and wraparounds
                VelX=0.2;
                VelY=absspeed;
                double target=30;
                double kp=.1;
                VelTwist=kp*(target-Io.navX.getYaw());
                if (Math.abs(VelTwist)>0.5)
                {
                    VelTwist=0.5*Math.signum(VelTwist);

                }
                
            }
            else
            {
                VelX=0;
                VelY=0;
                VelTwist=0;
            }
            if (Io.joystick.getRawButton(7))
            {
                Io.navX.zeroYaw();
            }
            Io.meccDrive.driveCartesian(VelX, VelY, VelTwist,-Io.navX.getYaw());
            return;
        }
       //Io.leftDriveMotor.set(Io.leftJoystick.getRawAxis(1));  //Notice the things that come from Io are referenced using the class name.
                                                            //Static variables are common to the class so, you use the class name to reference them.
       //Io.rightDriveMotor.set(Io.rightJoystick.getRawAxis(1));

      //Notice also that if you compare this code to last year's code, last year this class had an instance
      //of Joystick inside it, and it got initialized in the class constructor.  This year, we are calling
      //the static member of the Io class.
      //Neither way is "right"  or "wrong".  Both do the same thing.  Which one to use depends on a lot of things
      //like whether you intend to reuse the code somewhere else, versus the chance of making a mistake if code has
      //to change right now, very fast.  For us, we won't reuse the code, and we may very well have to make 
      //changes in a very short time.  If we do it this way, there's a slightly smaller chance of error.

      //It really comes down to personal preference and whatever the team agrees to.

      //TODO:  To switch back and forth between field centered and robot centered,
      //there has to be some adjustment in yaw calculations.
      if (drivingMode==DriveCoordinates.FIELD_CENTERED)
      {
      Io.meccDrive.driveCartesian(deadbanded(Io.joystick.getRawAxis(0),AXISDEADBAND),
       -1 * deadbanded(Io.joystick.getRawAxis(1),AXISDEADBAND), deadbanded(Io.joystick.getRawAxis(2),TWISTDEADBAND),Io.navX.getAngle());
      }
      else
      {

        double correctionFactor;

        //If we aren't commanding a turn, and we have stopped any previous turn
        if ((deadbanded(Io.joystick.getRawAxis(2),TWISTDEADBAND)==0)&&
            (Math.abs(Io.navX.getRate())<ROTATIONTHRESHOLD))
            {//We are trying to go straight
              correctionFactor=-1*CORRECTIONCONSTANT*Io.navX.getYaw();

            }
        else
        {
            correctionFactor=0;
            Io.navX.zeroYaw();

        }

            

        Io.meccDrive.driveCartesian(deadbanded(Io.joystick.getRawAxis(0),AXISDEADBAND),
        -1 * deadbanded(Io.joystick.getRawAxis(1),AXISDEADBAND), deadbanded(Io.joystick.getRawAxis(2),TWISTDEADBAND)+correctionFactor,0);
      }

    
    

    }

    /**
     * This function is used to drive under computer control, and set the values of the
     * motors based on desired speed and rotation.
     * @param desiredSpeed - The forward rate of motion, -1 to 1
     * @param desiredRotationRate -The turn rate, -1 to 1
     */
    public void driveAutonomous(double desiredSpeed, double desiredRotationRate)
    {
        //Calculate the desired motor speeds based on the desired input speeds
    }

    public enum DriveCoordinates
    {
    ROBOT_CENTERED,
    FIELD_CENTERED
    }

}

