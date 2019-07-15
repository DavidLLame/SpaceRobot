package frc.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * User commands
 * 
 * In time, this will take over all joystick commands
 * that started out in IO.  The goal is to be able to rapidly change
 * joysticks.  Our functional code (for driving, elevator, etc) should not be dependent
 * on a specific joystick.
 * 
 * The code is being written incrementally, until the final transition can be made.
 * 
 * As much as possible, this should read the joysticks directly and provide raw values.  Deadbands, if used, will be applied here, because those could change
 * based on which joystick is used.  However,
 * it may be that some inputs will be processed, in case two different joysticks might present the values differently
 */
public class UserCom
{





    private static Joystick driveStick;
    private static Joystick turnStick;
    private static Joystick driver2Stick;//The controller held in the hand of driver 2
    private static Joystick fightStick;//Fight Stick buttons for driver 2

    private static int DRIVESTICKPORT=0;
    private static int TURNSTICKPORT=1;
    private static int DRIVER2STICKPORT=2;
    private static int FIGHTSTICKPORT=3;



    /************************************************************************* */
    /*****************************DECLARATIONS FOR PRIMARY DRIVE STICK *******************/
    private static int DRIVEAXISX=0;
    private static int DRIVEAXISY=1;
    private static int DRIVEAXISTHROTTLE=3;

    private static int JSB_DRIVEMODE=2;//Toggles field/rocket

    private static int JSB_FIELDDRIVETOFORWARD=4;//arbitrary.  On turn stick

    private static int JSB_SAFETYOVERRIDE_DRIVER=11;
    



    private static double XDRIVEDEADBAND=0.15;
    private static double YDRIVEDEADBAND=0.15;
    private static double TWISTDEADBAND=0.5;
   

//Turn Stick
/*****************************DECLARATIONS FOR TURN STICK *******************/
private static int DRIVEAXISTWIST=0; //X Axis of the stick

  /*************************************************************************** */
/************************DECLARATIONS FOR DRIVER 2 Stick  (game controller) */

 


    private static double ELEVATORDEADBAND=0.35;

    private static int  MANUALAXISELEVATOR=1;
    private static int BEAVERTAIL_FIRE_AXIS=2;
    public static final int JSB_BEAVERTAILFIRE=2;
    private static final int JSB_RESETCARRIAGE=4;
    public static final int JSB_BEAVERTAILLOWER=5;
    private static int JSB_SAFETYOVERRIDE_D2=8;
    private static int JSB_SAFETYRESTORE=7;
    private static int PISTONBACK=3;


    
    private static int PRIMARY_FIRE_BUTTON=1;
    private static int INTAKE_BUTTON_POV = 180;
    private static int INTAKE_BUTTONL_POV=225;
    private static int INTAKE_BUTTONR_POV=135;
    private static int HATCH_BUTTON_POV=0;
    private static int HATCH_BUTTON_POVL=315;
    private static int HATCH_BUTTON_POVR=45;


/***************************************************************** */
/********DECLARATIONS FOR FIGHT STICK */
//Street Fighter


    private static final int JSB_LEVEL1HATCH=1;
    private static final int JSB_LEVEL2HATCH=2;
    private static final int JSB_LEVEL3HATCH=3;
    private static final int JSB_HATCKPICKUPLIFT=4;
    private static final int JSB_LEVEL1CARGO=5;
    private static final int JSB_LEVEL2CARGO=6;
    private static final int JSB_LEVEL3CARGO=7;
    private static final int JSB_CARGOPICKUPPRESET=8;

    




    
    

    
    public static boolean intakeButtons(){
        if(driver2Stick.getPOV() == INTAKE_BUTTONL_POV || driver2Stick.getPOV() == INTAKE_BUTTONR_POV || driver2Stick.getPOV() == INTAKE_BUTTON_POV )
        {
           return true;
            
        }
        else{
            return false;
        }
    }

    public static boolean hatchPickup()
    {
        if ((driver2Stick.getPOV()==HATCH_BUTTON_POV) ||
            (driver2Stick.getPOV()==HATCH_BUTTON_POVL) ||
            (driver2Stick.getPOV()==HATCH_BUTTON_POVR))
            return true;
            else
            {
                return false;
            }
    }

/************************************************** */

    /**
     * init() sets up the joysticks, because it refers to io, it should be called after Io init,
     * at least until the transition is complete
     */
    public static void init()
    {
        driveStick=new Joystick(DRIVESTICKPORT);//Temporarily, leave the instantiation in Io
        driver2Stick=new Joystick(DRIVER2STICKPORT);
        fightStick=new Joystick(FIGHTSTICKPORT);
        turnStick=new Joystick(TURNSTICKPORT);
    }

    public static double xDrive()
{

    
    return linearDeadband(driveStick.getRawAxis(DRIVEAXISX),XDRIVEDEADBAND);
}


/**
 * Y - Axis - includes inversion (mult by -1)
 * @return
 */
    public static double yDrive()
    {
        return -1*linearDeadband( driveStick.getRawAxis(DRIVEAXISY),YDRIVEDEADBAND);
    }

    public static double twistDrive()
    {
        return linearDeadband(turnStick.getRawAxis(DRIVEAXISTWIST),TWISTDEADBAND);
       
    }


    /**
     * Return a throttle value between 0 and 1, based on an axis
     * @return
     */
    public static double throttle()
    {
        return ((1+driveStick.getRawAxis(DRIVEAXISTHROTTLE))/2.0);
    }


    public static double manualElevatorSpeed()
    {
      
        return linearDeadband(driver2Stick.getRawAxis(MANUALAXISELEVATOR),ELEVATORDEADBAND);
    }

 /**
     * if the stick is in the deadband, output 0
     * If the stick is at 1, output 1 
     * Between the deadband limit and 1, scale linearly
     * 
     * @param raw The raw joystick value
     * @return The scaled value
     */
    private static double linearDeadband(double raw, double deadband)
    {

        if (Math.abs(raw)<deadband) return 0;

       return Math.signum(raw)*(Math.abs(raw)-deadband)/(1-deadband);
    }

    public static boolean Level1Cargo()
    {
        return fightStick.getRawButton(JSB_LEVEL1CARGO);
    }

    public static boolean Level2Cargo()
    {
        return fightStick.getRawButton(JSB_LEVEL2CARGO);
    }

    public static boolean Level3Cargo()
    {
        return fightStick.getRawButton(JSB_LEVEL3CARGO);
    }

    public static boolean Level1Hatch()
    {
        return fightStick.getRawButton(JSB_LEVEL1HATCH);
    }

    public static boolean Level2Hatch()
    {
        return fightStick.getRawButton(JSB_LEVEL2HATCH);
    }

    public static boolean Level3Hatch()
    {
        return fightStick.getRawButton(JSB_LEVEL3HATCH);
    }

    public static boolean HatchPickupLift()
    {
        return fightStick.getRawButton(JSB_HATCKPICKUPLIFT);
    }

    public static boolean CargoPickupPreset()
    {
        return fightStick.getRawButton(JSB_CARGOPICKUPPRESET);
    }

    /**
     * The fire command from the intake/hatch placer
     */
    public static boolean primaryFire()
    {
       return driver2Stick.getRawButton(PRIMARY_FIRE_BUTTON);
    }



    public static boolean beaverTailFire()
    {
       // return driver2Stick.getRawButton(JSB_BEAVERTAILFIRE);
        return (driver2Stick.getRawAxis(BEAVERTAIL_FIRE_AXIS)>0.9);
    }

    public static boolean beaverTailLower()
    {
        return driver2Stick.getRawButton(JSB_BEAVERTAILLOWER);
    }

    public static boolean resetElevatorZero()
    {
        return false;
     //   return driver2Stick.getRawButton(JSB_ELEVATORZERO);
    }


    public static boolean resetCarriageState()
    {
        return driver2Stick.getRawButton(JSB_RESETCARRIAGE);
    }


    public static boolean driveModeSwitch()
    {

        return driveStick.getRawButton(JSB_DRIVEMODE);
    }

    public static boolean fieldDriveToForward()
    {
        return driveStick.getRawButton(JSB_FIELDDRIVETOFORWARD);
    }


    public static boolean elevatorSafetyOverRideDriver1()
    {
        return driveStick.getRawButton(JSB_SAFETYOVERRIDE_DRIVER);
    }

    public static boolean elevatorSafetyOverRideDriver2()
    {
        return driveStick.getRawButton(JSB_SAFETYOVERRIDE_D2);
    }

    public static boolean restoreSafety()
    {
        return driver2Stick.getRawButton(JSB_SAFETYRESTORE);
    }

    public static boolean pullBackLocatorPiston()
    {
        return driver2Stick.getRawButton(PISTONBACK);
    }




}