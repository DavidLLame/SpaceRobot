package frc.robot;


import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import java.io.PrintWriter;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

//URL for spark max motor controller
//https://www.revrobotics.com/content/sw/max/sdk/REVRobotics.jso



public class Io {
  

    

  

    /**
     *
     */



    private static final int INTAKE_PWMPORT_BBOT=4;
    public static final int INTAKE_PDP_CHANNEL=12;  //The PDP channel where the intake motor is plugged in.  
                                                   //Note public.  Used in Manip.java





 






    //Notice that the following are not "final"


    private static final int FRONTLEFTMOTOR_PWMPORT = 2;
    private static final int REARRIGHTMOTOR_PWMPORT = 1;
    private static final int FRONTRIGHTMOTOR_PWMPORT = 0;
    private static final int REARLEFTMOTOR_PWMPORT = 3;
    
    private static int INTAKE_PWMPORT=4;
    private static int ELEVATOR_CANID=10;


    private static int SHOOTERSOLENOID=1;
    private static int LASTHOPESOLENOID=0;
    private static final int BEAVERTAILLOWER=2;
    private static final int BEAVERTAILFIRE=3;

    private static final SerialPort.Port JEVOISPORT=Port.kUSB1;

    private static final String DEBUGFILENAME="debug.txt";
    
    




    public static MecanumDrive meccDrive;
    public static boolean MecanumIsSet=false;

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

       public static SerialPort jevoisPort;

       private static PrintWriter debugWriter;

    




      
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
       
        elevatorEncoder=new CANEncoder(elevator); 
        elevatorController=new CANPIDController(elevator);


        navX = new AHRS(SerialPort.Port.kMXP);
        navX.zeroYaw();

        shoot1 = new Solenoid(SHOOTERSOLENOID);
        lasthope = new Solenoid(LASTHOPESOLENOID);
        beaverTailLower=new Solenoid(BEAVERTAILLOWER);
        beaverTailFire=new Solenoid(BEAVERTAILFIRE);

        pdp=new PowerDistributionPanel();
        
        try
        {
        jevoisPort=new SerialPort(115200,JEVOISPORT);
        }
        catch(Exception ex)
        {
            System.out.println("Exception creating jevoisport");
            throw ex; //remove after testing.  Deliberately crash things.
        }

        try
        {
        debugWriter=new PrintWriter(DEBUGFILENAME);
        }
        catch(Exception ex)
        {
           // Io.writeToDebug("File not found exception for debug file");
        }
        
    
        
    }


    /**
     * After the Mecanum Drive object is initialized, it must be "fed"
     * frequently or it will fail.  Rather than "feeding" it in RobotPeriodi,
     * we will wait to initialize it 
     */
    public static void initMecanum()
    {
        

            meccDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

       
        

    }

    private static boolean printDebugStrings=false;
    public static void printDebugMessage(String st)
    {
        if (printDebugStrings)
        {
            System.out.println(st);
        }
    }

    private static int flushcount=0;
    public static void writeToDebug(String st)
    {
        if (debugWriter!=null)
        {
        debugWriter.println(st);
        flushcount++;
        if (flushcount%20==0)
           debugWriter.flush();
        }

    }

    public static void closeDebugFile()
    {
        debugWriter.close();
    }


}