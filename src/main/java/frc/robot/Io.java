package frc.robot;


import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

//URL for spark max motor controller
//https://www.revrobotics.com/content/sw/max/sdk/REVRobotics.jso



public class Io  {
  

    

    //This class will be used to do all hardware initialization.
    //The lines below will be changed as we move forward to replace their current descriptions
    //with a description of the hardware that will be attached to them.  For example, if 
    //we add a motor drive on the left side and attach it to PWM Port 0, 
    //The line that says  public static final int PWMPORT_0=0;
    //will be replaced by  public static final int LEFTSIDEMOTOR_PWMPORT=0;


    /**
     *
     */



    private static final int INTAKE_PWMPORT_BBOT=4;





 






    //Notice that the following are not "final"
    public static  int FRONTLEFTMOTOR_PWMPORT = 2;
    public static int REARRIGHTMOTOR_PWMPORT = 1;
    public static  int FRONTRIGHTMOTOR_PWMPORT = 0;
    public static  int REARLEFTMOTOR_PWMPORT = 3; 
    
    private static int INTAKE_PWMPORT=4;
    private static int ELEVATOR_CANID=10;


    private static int SHOOTERSOLENOID=1;
    private static int LASTHOPESOLENOID=0;
    private static final int BEAVERTAILLOWER=2;
    private static final int BEAVERTAILFIRE=3;
    
    




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


       public static Solenoid shoot1;
       public static Solenoid lasthope;
       public static Solenoid beaverTailLower;
       public static Solenoid beaverTailFire;

       public static SpeedController intake;
       public static CANSparkMax elevator;
       public static CANEncoder elevatorEncoder;
       public static CANPIDController elevatorController;

       public static PowerDistributionPanel pdp;


      
       public static AHRS navX;

      public static double deadband;

<<<<<<< HEAD
      public static boolean isTestBench()
      {
          return false;
      }
=======
      private static boolean MecanumIsSet=false;

>>>>>>> fe136014161808ab36773ecbe504fa6f2533ba87

      public static boolean isBBot()
      {
          return true;
      }

      public static boolean isABot()
      {
          return false;
      }

<<<<<<< HEAD
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
              System.out.println("Setting up bbot");
            FRONTLEFTMOTOR_PWMPORT=FRONTLEFTMOTOR_PWMPORT_BBOT;
            FRONTRIGHTMOTOR_PWMPORT=FRONTRIGHTMOTOR_PWMPORT_BBOT;
            REARLEFTMOTOR_PWMPORT=REARLEFTMOTOR_PWMPORT_BBOT;
            REARRIGHTMOTOR_PWMPORT=REARRIGHTMOTOR_PWMPORT_BBOT;

            INTAKE_PWMPORT=INTAKE_PWMPORT_BBOT;
          }
      }

=======
      
>>>>>>> fe136014161808ab36773ecbe504fa6f2533ba87
    //This function will instantiate  all of the hardware variables declared above. And all any
    //Initialization functions needed.
    public static void initIO()
    {  //leftDriveMotor =new Talon(LEFTSIDEMOTOR_PWMPORT);  //Notice we don't say Victor(0).  That makes it easier to change.
                                                           //If we always use the symbolic names, we can change what port they are plugged in to
                                                           //by only changing one line of code.
                                             //All of the startup code for each object is in one place.

        //Repeat for all of the objects.     
        

        frontLeftMotor = new Victor(FRONTLEFTMOTOR_PWMPORT);
        frontLeftMotor.setInverted(true);
        rearLeftMotor = new Victor(REARLEFTMOTOR_PWMPORT);
        rearLeftMotor.setInverted(true);
        frontRightMotor = new Victor(FRONTRIGHTMOTOR_PWMPORT);
        frontRightMotor.setInverted(true);
        rearRightMotor = new Victor(REARRIGHTMOTOR_PWMPORT);
        rearRightMotor.setInverted(true);


            

        intake=new Victor(INTAKE_PWMPORT);

        elevator=new CANSparkMax(ELEVATOR_CANID, MotorType.kBrushless);
       
       

      //  elevatorEncoder=new CANEncoder(elevator); 
      //  elevatorController=new CANPIDController(elevator);


    


      //  navX = new AHRS(SerialPort.Port.kUSB1);
     //   navX.zeroYaw();

        shoot1 = new Solenoid(SHOOTERSOLENOID);
        lasthope = new Solenoid(LASTHOPESOLENOID);
        beaverTailLower=new Solenoid(BEAVERTAILLOWER);
        beaverTailFire=new Solenoid(BEAVERTAILFIRE);

        //pdp=new PowerDistributionPanel();
        

    
        
    }


    /**
     * After the Mecanum Drive object is initialized, it must be "fed"
     * frequently or it will fail.  Rather than "feeding" it in RobotPeriodi,
     * we will wait to initialize it 
     */
    public static void initMecanum()
    {
   /*     if (MecanumIsSet) return;
        else
        {

            meccDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
            MecanumIsSet=true;
      8 
        }*/

    }

    private static boolean printDebugStrings=true;
    public static void printDebugMessage(String st)
    {
        if (printDebugStrings)
        {
            System.out.println(st);
        }
    }


}