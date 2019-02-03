package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
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

    /**
     *
     */

    private static final int FRONTLEFTMOTOR_PWMPORT_TBENCH = 4;
    private static final int REARRIGHTMOTOR_PWMPORT_TBENCH = 2;
    private static final int FRONTRIGHTMOTOR_PWMPORT_TBENCH = 3;
    private static final int REARLEFTMOTOR_PWMPORT_TBENCH = 5;

    private static final int FRONTLEFTMOTOR_PWMPORT_BBOT = 4;
    private static final int REARRIGHTMOTOR_PWMPORT_BBOT = 2;
    private static final int FRONTRIGHTMOTOR_PWMPORT_BBOT = 3;
    private static final int REARLEFTMOTOR_PWMPORT_BBOT = 5;

    private static final int INTAKE_PWMPORT_TBENCH=1;
    private static final int INTAKE_PWMPORT_BBOT=1;

    //Joystick buttons
    public static final int FIRE_BUTTON=10;
    public static final int INTAKE_BUTTON=11;

    //The joystick buttons for hatch selection
    public static final int JSB_LEVEL1HATCH=7;
    public static final int JSB_LEVEL2HATCH=9;
    public static final int JSB_LEVEL3HATCH=11;
    public static final int JSB_LEVEL1CARGO=8;
    public static final int JSB_LEVEL2CARGO=10;
    public static final int JSB_LEVEL3CARGO=12;
    public static final int MANUALOVERRIDEELEVATOR=7;
    public static final int AUTOMATICOPERATIONELEVATOR=8;

    public static final int MANUALAXISELEVATOR=1;




    //Notice that the following are not "final"
    public static  int FRONTLEFTMOTOR_PWMPORT = 4;
    public static int REARRIGHTMOTOR_PWMPORT = 2;
    public static  int FRONTRIGHTMOTOR_PWMPORT = 3;
    public static  int REARLEFTMOTOR_PWMPORT = 5; 
    
    private static int INTAKE_PWMPORT=1;
    private static int ELEVATOR_CANID=10;


    private static int SHOOTERSOLENOID=1;
    private static int LASTHOPESOLENOID=0;
    
    


    //After the declarations of constants, we'll declare variables, but not initialize them yet.  We'll keep the initializations
    //in a single initialization function.

    //Here's some examples: (in comments.  They'll be replaced by real ones as we move on.)
    
    
    //  Note:  These are declarations.  Not instantiations.  Learn the difference.  It will save a lot of 
    //  NullReferenceException errors.
    //  public static Joystick leftStick;  
    //  public static Joystick rightStick;

    public static MecanumDrive meccDrive;

    //All functions of this class should be declared as static.
    //We will never have more than one of this class, so we'll just use statics.

    //Notice that the motor controller objects are declared as SpeedController objects, not
    //as sparks, victors, talons, etc.  That way, we can use any of them, and reuse the same 
    //code on a-bot and b-bot, even though they have different controllers.
      public  static SpeedController frontLeftMotor;
      public   static SpeedController rearLeftMotor;
      public  static SpeedController frontRightMotor;
      public  static SpeedController rearRightMotor;

       public static Joystick joystick;
       public static Joystick elevatorStick;

       public static Solenoid shoot1;
       public static Solenoid lasthope;

       public static SpeedController intake;
       public static CANSparkMax elevator;
       public static CANEncoder elevatorEncoder;
       public static CANPIDController elevatorController;


      
       public static AHRS navX;

      public static double deadband;


      public static boolean isTestBench()
      {
          return true;
      }

      public static boolean isBBot()
      {
          return false;
      }

      public static boolean isABot()
      {
          return false;
      }

      private static void setSpecificRobot()
      {
          if (isTestBench())
          {
              FRONTLEFTMOTOR_PWMPORT=FRONTLEFTMOTOR_PWMPORT_TBENCH;
              FRONTRIGHTMOTOR_PWMPORT=FRONTRIGHTMOTOR_PWMPORT_TBENCH;
              REARLEFTMOTOR_PWMPORT=REARLEFTMOTOR_PWMPORT_TBENCH;
              REARRIGHTMOTOR_PWMPORT=REARRIGHTMOTOR_PWMPORT_TBENCH;

              INTAKE_PWMPORT=INTAKE_PWMPORT_TBENCH;
          }

          else 
          {
            FRONTLEFTMOTOR_PWMPORT=FRONTLEFTMOTOR_PWMPORT_BBOT;
            FRONTRIGHTMOTOR_PWMPORT=FRONTRIGHTMOTOR_PWMPORT_BBOT;
            REARLEFTMOTOR_PWMPORT=REARLEFTMOTOR_PWMPORT_BBOT;
            REARRIGHTMOTOR_PWMPORT=REARRIGHTMOTOR_PWMPORT_BBOT;

            INTAKE_PWMPORT=INTAKE_PWMPORT_BBOT;
          }
      }
    //This function will instantiate  all of the hardware variables declared above. And all any
    //Initialization functions needed.
    public static void initIO()
    {
        //leftDriveMotor =new Talon(LEFTSIDEMOTOR_PWMPORT);  //Notice we don't say Victor(0).  That makes it easier to change.
                                                           //If we always use the symbolic names, we can change what port they are plugged in to
                                                           //by only changing one line of code.
                                             //All of the startup code for each object is in one place.

        //Repeat for all of the objects.     
        
        if (isTestBench()||isBBot())
        {
        frontLeftMotor = new Victor(FRONTLEFTMOTOR_PWMPORT);
        rearLeftMotor = new Victor(REARLEFTMOTOR_PWMPORT);
        frontRightMotor = new Victor(FRONTRIGHTMOTOR_PWMPORT);
        rearRightMotor = new Victor(REARRIGHTMOTOR_PWMPORT);

        intake=new Victor(INTAKE_PWMPORT);
        }
        else
        {
        frontLeftMotor = new Spark(FRONTLEFTMOTOR_PWMPORT);
        rearLeftMotor = new Spark(REARLEFTMOTOR_PWMPORT);
        frontRightMotor = new Spark(FRONTRIGHTMOTOR_PWMPORT);
        rearRightMotor = new Spark(REARRIGHTMOTOR_PWMPORT);

        intake=new Spark(INTAKE_PWMPORT);
        }

        elevator=new CANSparkMax(ELEVATOR_CANID, MotorType.kBrushless);
        elevatorEncoder=new CANEncoder(elevator); 
        elevatorController=new CANPIDController(elevator);

    
        meccDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
        
        joystick = new Joystick(1);
        elevatorStick=joystick;

        navX = new AHRS(SerialPort.Port.kUSB1);
        navX.zeroYaw();

        shoot1 = new Solenoid(SHOOTERSOLENOID);
        lasthope = new Solenoid(LASTHOPESOLENOID);
    
        

    }


}