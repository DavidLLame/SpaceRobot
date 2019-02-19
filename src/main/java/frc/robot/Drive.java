package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    private final double Kp=0.03;
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

    private double myRate()
    {
        double diff=Io.navX.getAngle()-lastAngle;
        long elapsed =System.currentTimeMillis()-lastcalltime;
        if (diff>180) diff-=360;
        else if (diff<-180) diff+=360;
        return 1000.0*diff/elapsed;



    }

    public Drive()
    {
        rotationOutput=new MecPIDOutput();
        rotatePidController=new PIDController(Kp, Ki, Kd, Io.navX, rotationOutput);
        rotatePidController.setInputRange(-180, 180);
        rotatePidController.setContinuous();
        rotatePidController.setSetpoint(0.0);
        rotatePidController.setOutputRange(-1.0, 1.0);
        rotatePidController.enable();
    
    }

    public void Init()
    {
        rotatePidController.setSetpoint(Io.navX.getYaw());
        lastIsRecorded=false;
    }

    private boolean drivemodebuttonpressed=false;
    /**
     * This function is called to do "normal", teleperated, driving.
     */
    public void driveByJoystick()
    {
        SmartDashboard.putString("Turning State",turningState.toString());
        SmartDashboard.putNumber("PID Output",rotationOutput.getRotationCorrection());
        SmartDashboard.putNumber("SetPoint", rotatePidController.getSetpoint());
        SmartDashboard.putNumber("Yaw", Io.navX.getYaw());
        SmartDashboard.putNumber("Stick Twist",UserCom.twistDrive());
        SmartDashboard.putNumber("Turn Rate",Io.navX.getRate());

        
        computeTurningState();
        ComputeDriveMode();
        SmartDashboard.putString("Drive coordinates",drivingMode.toString());
        SmartDashboard.putString("Turn Mode", turningState.toString());
    
        System.out.println("Driving by joystick");
        

        double throttleMultiplier=UserCom.throttle();
      //TODO:  To switch back and forth between field centered and robot centered,
      //there has to be some adjustment in yaw calculations.
      if (drivingMode==DriveCoordinates.FOURTYFIVE)
      {
          if (UserCom.yDrive()>0.5)
          {
              Io.meccDrive.driveCartesian(-0.7, .7, rotationOutput.getRotationCorrection(),-1*Io.navX.getAngle());
          }
          else
          {
              Io.meccDrive.driveCartesian(0, 0, 0,0);
          }
      }


      else if (drivingMode==DriveCoordinates.FIELD_CENTERED)
      {if (turningState==TurnCommand.DRIVE_STRAIGHT)
        {
          Io.meccDrive.driveCartesian(throttleMultiplier*UserCom.xDrive(),throttleMultiplier*UserCom.yDrive(),rotationOutput.getRotationCorrection(),-1*Io.navX.getAngle());
        }
          else
         { 
          Io.meccDrive.driveCartesian(throttleMultiplier*UserCom.xDrive(),throttleMultiplier*UserCom.yDrive(),UserCom.twistDrive(),-1*Io.navX.getAngle());
         }
      }
      else if (drivingMode==DriveCoordinates.ROBOT_CENTERED)
      {

        if (turningState==TurnCommand.DRIVE_STRAIGHT)        
        {
        Io.meccDrive.driveCartesian(UserCom.xDrive(),UserCom.yDrive(),rotationOutput.getRotationCorrection(),0);
        }
        else
        {
            Io.meccDrive.driveCartesian(UserCom.xDrive(),UserCom.yDrive(),UserCom.twistDrive(),0);
  
        }
      }
      else
      {
          Io.meccDrive.driveCartesian(UserCom.xDrive(), UserCom.yDrive(), UserCom.twistDrive(),0);
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

    public void driveToAngle(double newAngle)
    {
        double currentAngle=Io.navX.getAngle();
        double error=newAngle-currentAngle;
        System.out.println("Error: "+error+" Current: "+currentAngle);
        if (Math.abs(error)>2)
        {
        Io.meccDrive.driveCartesian(0, 0, error*0.05,0);
        }
    }

    public void sitStill()
{
     Io.meccDrive.driveCartesian(0, 0, 0);
}

    /**
     * Translate throttle stick value from -1 to 1, to 0 to 1
     * @param axisValue
     * @return
     */
    public double throttleLevel(double axisValue)
    { return (axisValue+1)/2.0;}


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

