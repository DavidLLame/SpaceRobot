package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import edu.wpi.first.wpilibj.SerialPort;
public class Io  {
  

    //This class will be used to do all hardware initialization.
    //The lines below will be changed as we move forward to replace their current descriptions
    //with a description of the hardware that will be attached to them.  For example, if 
    //we add a motor drive on the left side and attach it to PWM Port 0, 
    //The line that says  public static final int PWMPORT_0=0;
    //will be replaced by  public static final int LEFTSIDEMOTOR_PWMPORT=0;

    public static final int DIOPORT_0=0;
    public static final int DIOPORT_1=1;
    public static final int DIOPORT_2=2;
    public static final int DIOPORT_3=3;
    public static final int DIOPORT_4=4;
    public static final int DIOPORT_5=5;
    public static final int DIOPORT_6=6;
    public static final int DIOPORT_7=7;
    public static final int DIOPORT_8=8;
    public static final int DIOPORT_9=9;

    public static final int PWMPORT_0=0;
    public static final int PWMPORT_1=1;
    public static final int PWMPORT_2=2;
    public static final int PWMPORT_3=3;
    public static final int PWMPORT_4=4;
    public static final int PWMPORT_5=5;
    public static final int PWMPORT_6=6;
    public static final int PWMPORT_7=7;
    public static final int PWMPORT_8=8;
    public static final int PWMPORT_9=9;

    public static final int AINPORT_0=0;
    public static final int AINPORT_1=1;
    public static final int AINPORT_2=2;
    public static final int AINPORT_3=3;
    public static final int FRONTLEFTMOTOR_PWMPORT = 4;
    private static final int REARRIGHTMOTOR_PWMPORT = 2;
    private static final int FRONTRIGHTMOTOR_PWMPORT = 3;
    private static final int REARLEFTMOTOR_PWMPORT = 5;
    


    //After the declarations of constants, we'll declare variables, but not initialize them yet.  We'll keep the initializations
    //in a single initialization function.

    //Here's some examples: (in comments.  They'll be replaced by real ones as we move on.)
    
    public static  SpeedController leftDriveMotor;  //A SpeedController is a generic motor.
    //  Note:  These are declarations.  Not instantiations.  Learn the difference.  It will save a lot of 
    //  NullReferenceException errors.
    //  public static Joystick leftStick;  
    //  public static Joystick rightStick;

    public static MecanumDrive meccDrive;

    //All functions of this class should be declared as static.
    //We will never have more than one of this class, so we'll just use statics.
        static Victor frontLeftMotor;
        static Victor rearLeftMotor;
        static Victor frontRightMotor;
        static Victor rearRightMotor;

       public static Joystick joystick;

       
       public static AHRS navX;

      public static double deadband;

    //This function will instantiate  all of the hardware variables declared above. And all any
    //Initialization functions needed.
    public static void initIO()
    {
        //leftDriveMotor =new Talon(LEFTSIDEMOTOR_PWMPORT);  //Notice we don't say Victor(0).  That makes it easier to change.
                                                           //If we always use the symbolic names, we can change what port they are plugged in to
                                                           //by only changing one line of code.
                                             //All of the startup code for each object is in one place.

        //Repeat for all of the objects.     
        
       
        frontLeftMotor = new Victor(FRONTLEFTMOTOR_PWMPORT);
        rearLeftMotor = new Victor(REARLEFTMOTOR_PWMPORT);
        frontRightMotor = new Victor(FRONTRIGHTMOTOR_PWMPORT);
        rearRightMotor = new Victor(REARRIGHTMOTOR_PWMPORT);
    
        meccDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
        
        joystick = new Joystick(1);

        navX = new AHRS(SerialPort.Port.kUSB1);

        navX.zeroYaw();

        

    }


}