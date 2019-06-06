package frc.robot;

import edu.wpi.first.wpilibj.PIDController;

/**
 * The class for all driving related functions
 */
public class Drive{

    private final double AXISDEADBAND=0.1;
    private final double TWISTDEADBAND=0.5;
    private final double ROTATIONTHRESHOLD=3;//10 degrees per second.  Threshold to determine if rtation has stopped.
    private final double CORRECTIONCONSTANT=.01;//Kp of a rotation PID control

    public DriveCoordinates drivingMode=DriveCoordinates.FIELD_CENTERED;
    public boolean naxXDisabled=true;

    PIDController rotatePidController;
    private final double Kp=0.1;
    private final double Kd=0.0;
    private final double Ki=0.0;
    MecPIDOutput rotationOutput;

    long stateChangeTime;

    private TurnCommand turningState=TurnCommand.DRIVE_STRAIGHT;

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

    boolean overrideDriver=false;

    private double lastAngle;
    private long lastcalltime;
    private boolean lastIsRecorded=false;

    private double SAFETY_HEIGHT=38;
    long departureTime;
    double safetyThrottle;

    public Drive()
    {
        rotationOutput=new MecPIDOutput();
        rotatePidController=new PIDController(Kp, Ki, Kd, Io.navX, rotationOutput);
        rotatePidController.setInputRange(-180, 180);
        rotatePidController.setContinuous();
        rotatePidController.setSetpoint(0.0);
        rotatePidController.setOutputRange(-0.6, 0.6);
        rotatePidController.setAbsoluteTolerance(4);
        rotatePidController.enable();
    
    }

    public void Init()
    {


        rotatePidController.setSetpoint(Io.navX.getYaw());
        lastIsRecorded=false;
        drivingMode=DriveCoordinates.ROBOT_CENTERED;
        holdingDriveButton=false;
        
    

    }

    private boolean drivemodebuttonpressed=false;
    /**
     * This function is called to do "normal", teleperated, driving.
     */
    public void driveByJoystick()
    {
        
   
        double computedX=UserCom.xDrive();//All throttle references are gone
        double computedY=UserCom.yDrive();


        computeTurningState();
        ComputeDriveMode();

        

        

     
     

      if (drivingMode==DriveCoordinates.FIELD_CENTERED)
      {if (turningState==TurnCommand.DRIVE_STRAIGHT)
        {
            if(UserCom.fieldDriveToForward())
            {
                rotatePidController.setSetpoint(currentStickHeading());
            }
          Io.meccDrive.driveCartesian(computedX,computedY,rotationOutput.getRotationCorrection(),-1*Io.navX.getYaw());
        }
          else
         { 
          Io.meccDrive.driveCartesian(computedX,computedY,UserCom.twistDrive(),-1*Io.navX.getYaw());
         }
      }
      else if (drivingMode==DriveCoordinates.ROBOT_CENTERED)
      {

        if (turningState==TurnCommand.DRIVE_STRAIGHT)        
        {
        Io.meccDrive.driveCartesian(computedX,computedY,rotationOutput.getRotationCorrection(),0);
        }
        else
        {
            Io.meccDrive.driveCartesian(computedX,computedY,UserCom.twistDrive(),0);
  
        }
      }
      else
      {
          Io.meccDrive.driveCartesian(computedX, computedY, UserCom.twistDrive(),0);
      }


    
    

    }

    public void driveRaw(double x, double y, double rotation)
    {
        Io.meccDrive.driveCartesian(x,y,rotation);

        
    }

    public void driveByCamera(double x, double y, double theta)
    {
        rotatePidController.setSetpoint(Io.navX.getAngle()+theta);
        double SLOWDOWN=.5;
        double divisor=Math.max(Math.abs(x), Math.abs(y));
        double correction=rotationOutput.getRotationCorrection();
        System.out.println("Drive: "+SLOWDOWN*x/divisor+" "+SLOWDOWN*y/divisor+" "+correction);
        Io.meccDrive.driveCartesian(SLOWDOWN*x/divisor, SLOWDOWN*y/divisor, rotationOutput.getRotationCorrection(),0);
    }

    public void sitStill()
{
     Io.meccDrive.driveCartesian(0, 0, 0);
}




    private boolean holdingDriveButton=false;
    private void ComputeDriveMode()
    {
        if (!UserCom.driveModeSwitch()) holdingDriveButton=false;
        if (holdingDriveButton) return;//Whatever you were doing, you're still doing it.

        if (UserCom.driveModeSwitch())
        {
            holdingDriveButton=true;
            if (drivingMode==DriveCoordinates.FIELD_CENTERED)
            {
                drivingMode=DriveCoordinates.ROBOT_CENTERED;
            }
            else if (drivingMode==DriveCoordinates.ROBOT_CENTERED)
            {
                drivingMode=DriveCoordinates.FIELD_CENTERED;
            }
        }

        
    }


    public void computeTurningState()
    {
        switch(turningState)
        {
            case DRIVE_STRAIGHT:
               if (UserCom.twistDrive()!=0)
               {
                   changeState(TurnCommand.TURNING_COMMANDED);
               }
               break;
            case TURNING_COMMANDED:
            if (UserCom.twistDrive()==0)
            {
                changeState(TurnCommand.TURNING_INERTIA);
            }
            break;
            case TURNING_INERTIA:
            if (UserCom.twistDrive()!=0)
            {
                changeState(TurnCommand.TURNING_COMMANDED);
            }
            else if (System.currentTimeMillis()-stateChangeTime >400)
            {
                changeState(TurnCommand.DRIVE_STRAIGHT);
            }
            break;

        }
    }

    private void changeState(TurnCommand newState)
    {
        if (newState!=turningState)
        {
            stateChangeTime=System.currentTimeMillis();
        }


        if ((turningState!=TurnCommand.DRIVE_STRAIGHT)&&
        (newState==TurnCommand.DRIVE_STRAIGHT))
        {
            rotatePidController.setSetpoint(Io.navX.getYaw());
        }

        turningState=newState;
    }

    /**
     * Get the current compass heading dictated by the joysticks
     * @return heading in DEGREES
     */
    private double currentStickHeading()
    {

        double xDirection=UserCom.xDrive();
        double yDirection=UserCom.yDrive();

        
        if ((Math.abs(xDirection)==0)&&(Math.abs(yDirection)==0)) //If they aren't using the stick, keep pointed where you are
        {return Io.navX.getYaw();}

        
        //Now convert from X/Y plane to field heading coordinates
        double radiansHeading=Math.atan2(yDirection,xDirection);
        double degreesCartesianHeading=radiansHeading*180.0/Math.PI;
        double heading= 90-degreesCartesianHeading;
        if (heading>189) heading-=360;
        return heading;


    }

    public void disable()
    {

    }


    public enum DriveCoordinates
    {
    ROBOT_CENTERED,
    FIELD_CENTERED,
    RAWSTICK, //Gyro disabled.  Raw stick values
    FOURTYFIVE //For test only.
    }

    public enum TurnCommand
    {
        TURNING_COMMANDED,
        TURNING_INERTIA,
        DRIVE_STRAIGHT
    }

    

}