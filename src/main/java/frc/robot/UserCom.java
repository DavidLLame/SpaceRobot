package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

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


/*****************************DECLARATIONS FOR PRIMARY DRIVE STICK *******************/

    //Joysticks will be private when it's done, because
    //no other code ought to reference joysticks directly
    private static Joystick driveStick;
    private static int DRIVESTICKPORT=0;

    private static int JSB_DRIVEMODE=2;
    
    private static int DRIVEAXISX=0;
    private static int DRIVEAXISY=1;
    private static int DRIVEAXISTWIST=2;
    private static int DRIVEAXISTHROTTLE=3;



    private static double XDRIVEDEADBAND=0.3;
    private static double YDRIVEDEADBAND=0.3;
    private static double TWISTDEADBAND=0.5;

/************************DECLARATIONS FOR DRIVER 2 STICK */

    private static Joystick driver2Stick;//The controller held in the hand of driver 1

    private static int PRIMARY_FIRE_BUTTON=5;
    private static int INTAKE_BUTTON=4;

    private static int DRIVER2STICKPORT=1;
    private static int  MANUALAXISELEVATOR=1;
    private static double ELEVATORDEADBAND=0.35;

    public static final int JSB_LEVEL1HATCH=6;
    public static final int JSB_LEVEL2HATCH=7;
    public static final int JSB_LEVEL3HATCH=8;
    public static final int JSB_LEVEL1CARGO=11;
    public static final int JSB_LEVEL2CARGO=10;
    public static final int JSB_LEVEL3CARGO=9;
    public static final int JSB_ELEVATORZERO=4;
    public static final int JSB_ELEVATORAUTO=5;

    public static final int JSB_BEAVERTAILLOWER=1;
    public static final int JSB_BEAVERTAILFIRE=2;
/************************************************** */

    /**
     * init() sets up the joysticks, because it refers to io, it should be called after Io init,
     * at least until the transition is complete
     */
    public static void init()
    {
        driveStick=new Joystick(DRIVESTICKPORT);//Temporarily, leave the instantiation in Io
        driver2Stick=new Joystick(DRIVER2STICKPORT);
    }

    public static double xDrive()
{
    System.out.println("X drive "+ driveStick.getRawAxis(0));
    
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
        return linearDeadband(driveStick.getRawAxis(DRIVEAXISTWIST),TWISTDEADBAND);
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
        System.out.println("Requesting elevator speed");
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

        System.out.println("Deadband "+ raw+ " "+ "deadband");
       System.out.println("Ret value: "+ Math.signum(raw)*(Math.abs(raw)-deadband)/(1-deadband));
        if (Math.abs(raw)<deadband) return 0;

       return Math.signum(raw)*(Math.abs(raw)-deadband)/(1-deadband);
    }

    public static boolean Level1Cargo()
    {
        return driver2Stick.getRawButton(JSB_LEVEL1CARGO);
    }

    public static boolean Level2Cargo()
    {
        return driver2Stick.getRawButton(JSB_LEVEL2CARGO);
    }

    public static boolean Level3Cargo()
    {
        return driver2Stick.getRawButton(JSB_LEVEL3CARGO);
    }

    public static boolean Level1Hatch()
    {
        return driver2Stick.getRawButton(JSB_LEVEL1HATCH);
    }

    public static boolean Level2Hatch()
    {
        return driver2Stick.getRawButton(JSB_LEVEL2HATCH);
    }

    public static boolean Level3Hatch()
    {
        return driver2Stick.getRawButton(JSB_LEVEL3HATCH);
    }

    /**
     * The fire command from the intake/hatch placer
     */
    public static boolean primaryFire()
    {
       return driver2Stick.getRawButton(PRIMARY_FIRE_BUTTON);
    }

    public static boolean intakeMotorOn()
    {
        return driver2Stick.getRawButton(INTAKE_BUTTON);
    }

    public static boolean beaverTailFire()
    {
        return driver2Stick.getRawButton(JSB_BEAVERTAILFIRE);

    }

    public static boolean beaverTailLower()
    {
        return driver2Stick.getRawButton(JSB_BEAVERTAILLOWER);
    }

    public static boolean resetElevatorZero()
    {
        return driver2Stick.getRawButton(JSB_ELEVATORZERO);
    }

    public static boolean isElevatorAuto()
    {
        return driver2Stick.getRawButton(JSB_ELEVATORAUTO);
    }


    public static boolean driveModeSwitch()
    {

        return driveStick.getRawButton(JSB_DRIVEMODE);
    }




}