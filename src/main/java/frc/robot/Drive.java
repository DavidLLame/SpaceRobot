package frc.robot;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Relay.Value;


/**
 * The class for all driving related functions
 */
public class Drive{

    private final double sparkspeed=.75;
    private final double AXISDEADBAND=0.1;
    private final double TWISTDEADBAND=0.5;
    private final double ROTATIONTHRESHOLD=10;//10 degrees per second.  Threshold to determine if rtation has stopped.
    private final double CORRECTIONCONSTANT=.01;//Kp of a rotation PID control

    public static CANSparkMax sparkMotor;

   // public DriveCoordinates drivingMode=DriveCoordinates.FIELD_CENTERED;
    //public DriveCoordinates drivingMode=DriveCoordinates.ROBOT_CENTERED;
    public boolean naxXDisabled=true;

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

    boolean overrideDriver=false;
    /**
     * This function is called to do "normal", teleperated, driving.
     */
    public void driveByJoystick()
    {  


        System.out.println("Driving by joystick");
        
        double throttleMultiplier=throttleLevel(Io.driveStick.getRawAxis(Io.THROTTLEAXIS));
      //TODO:  To switch back and forth between field centered and robot centered,
      //there has to be some adjustment in yaw calculations.
     // if (drivingMode==DriveCoordinates.FIELD_CENTERED)
      {
      Io.meccDrive.driveCartesian(throttleMultiplier*deadbanded(Io.driveStick.getRawAxis(Io.DRIVEXAXIS),AXISDEADBAND),
       -1*throttleMultiplier * deadbanded(Io.driveStick.getRawAxis(1),AXISDEADBAND), deadbanded(Io.driveStick.getRawAxis(Io.DRIVETWISTAXIS),TWISTDEADBAND),Io.navX.getAngle());
      }
      //else
      {

        System.out.println("Driving robot centered");
        double correctionFactor;

        //If we aren't commanding a turn, and we have stopped any previous turn
        if (naxXDisabled)
        {
             correctionFactor=0;
        }
        if ((deadbanded(Io.driveStick.getRawAxis(Io.DRIVETWISTAXIS),TWISTDEADBAND)==0)&&
            (Math.abs(Io.navX.getRate())<ROTATIONTHRESHOLD))
            {//We are trying to go straight
              correctionFactor=-1*CORRECTIONCONSTANT*Io.navX.getYaw();

            }
        else
        {
            correctionFactor=0;
            Io.navX.zeroYaw();

        }

            

        
        Io.meccDrive.driveCartesian(deadbanded(Io.driveStick.getRawAxis(Io.DRIVEXAXIS),AXISDEADBAND),
        -1 * deadbanded(Io.driveStick.getRawAxis(Io.DRIVEYAXIS),AXISDEADBAND), deadbanded(Io.driveStick.getRawAxis(Io.DRIVETWISTAXIS),TWISTDEADBAND)+correctionFactor,0);
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


    /**
     * Translate throttle stick value from -1 to 1, to 0 to 1
     * @param axisValue
     * @return
     */
    public double throttleLevel(double axisValue)
    { return (axisValue+1)/2.0;}

    public enum DriveCoordinates
    {
    ROBOT_CENTERED,
    FIELD_CENTERED
    }


   // public void sparkthing () {
   //     Io.sparkMotor.set(sparkspeed);
        
    

   // public double sparkposition(){
        //Io.sparkEncoder.getPosition();
    
    

}
